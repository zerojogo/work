package com.example.demo.mapper;

import com.example.demo.entity.Directory;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zwf
 * @since 2018-09-21
 */
public interface DirectoryMapper extends BaseMapper<Directory> {

    Integer directoryMax(Map map);
}
