package com.zw.mapper;

import com.zw.domain.Ebook;
import com.zw.domain.EbookExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EbookMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ebook
     *
     * @mbg.generated Fri Dec 03 14:11:28 CST 2021
     */
    long countByExample(EbookExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ebook
     *
     * @mbg.generated Fri Dec 03 14:11:28 CST 2021
     */
    int deleteByExample(EbookExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ebook
     *
     * @mbg.generated Fri Dec 03 14:11:28 CST 2021
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ebook
     *
     * @mbg.generated Fri Dec 03 14:11:28 CST 2021
     */
    int insert(Ebook record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ebook
     *
     * @mbg.generated Fri Dec 03 14:11:28 CST 2021
     */
    int insertSelective(Ebook record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ebook
     *
     * @mbg.generated Fri Dec 03 14:11:28 CST 2021
     */
    List<Ebook> selectByExample(EbookExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ebook
     *
     * @mbg.generated Fri Dec 03 14:11:28 CST 2021
     */
    Ebook selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ebook
     *
     * @mbg.generated Fri Dec 03 14:11:28 CST 2021
     */
    int updateByExampleSelective(@Param("record") Ebook record, @Param("example") EbookExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ebook
     *
     * @mbg.generated Fri Dec 03 14:11:28 CST 2021
     */
    int updateByExample(@Param("record") Ebook record, @Param("example") EbookExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ebook
     *
     * @mbg.generated Fri Dec 03 14:11:28 CST 2021
     */
    int updateByPrimaryKeySelective(Ebook record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ebook
     *
     * @mbg.generated Fri Dec 03 14:11:28 CST 2021
     */
    int updateByPrimaryKey(Ebook record);
}