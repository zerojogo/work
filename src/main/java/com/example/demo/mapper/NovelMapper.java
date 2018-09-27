package com.example.demo.mapper;

import com.example.demo.entity.Novel;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zwf
 * @since 2018-09-21
 */
public interface NovelMapper extends BaseMapper<Novel> {

    Date getMysqlDate();

}
