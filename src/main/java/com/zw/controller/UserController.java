package com.zw.controller;

import com.zw.req.UserQueryReq;
import com.zw.req.UserQueryResp;
import com.zw.req.UserSaveReq;
import com.zw.resp.CommonResp;
import com.zw.resp.PageResp;
import com.zw.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {

        CommonResp resp = new CommonResp();
        return resp;
    }
}