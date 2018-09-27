package com.example.demo.service;

import com.example.demo.entity.Novel;
import com.baomidou.mybatisplus.service.IService;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zwf
 * @since 2018-09-21
 */
public interface INovelService extends IService<Novel> {

    Date getMysqlDate();
}
