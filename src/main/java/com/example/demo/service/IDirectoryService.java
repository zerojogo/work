package com.example.demo.service;

import com.example.demo.entity.Directory;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zwf
 * @since 2018-09-21
 */
public interface IDirectoryService extends IService<Directory> {

    Integer directoryMax(Map map);
}
