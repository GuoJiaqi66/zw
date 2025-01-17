package com.zw.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zw.domain.Content;
import com.zw.domain.ContentExample;
import com.zw.domain.Doc;
import com.zw.domain.DocExample;
import com.zw.exception.BusinessException;
import com.zw.exception.BusinessExceptionCode;
import com.zw.mapper.ContentMapper;
import com.zw.mapper.DocMapper;
import com.zw.mapper.DocMapperCust;
import com.zw.req.DocQueryReq;
import com.zw.req.DocSaveReq;
import com.zw.resp.DocQueryResp;
import com.zw.resp.PageResp;
import com.zw.util.CopyUtil;
import com.zw.util.RedisUtil;
import com.zw.util.RequestContext;
import com.zw.util.SnowFlake;
import com.zw.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.swing.text.AbstractDocument;
import java.util.List;

@Service
public class DocService {
    private static final Logger LOG =  LoggerFactory.getLogger(DocService.class);

    @Resource
    DocMapper docMapper;

    @Resource
    SnowFlake snowFlake;

    @Resource
    ContentMapper contentMapper;

    @Resource
    DocMapperCust docMapperCust;

    @Resource
    RedisUtil redisUtil;

    @Resource
    WsService wsService;


    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;
    }

    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


        // 列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    @Transactional
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            // 更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    /*public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }*/

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        // 阅读数+1
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }

    public void vote(Long id) {
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 60 * 60 * 24)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        Doc docDb = docMapper.selectByPrimaryKey(id);
        String logId = MDC.get("LOG_ID");
        wsService.sendInfo("【" + docDb.getName() + "】" + "被点赞", logId);

    }

}
