package com.example.demo.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.demo.entity.Directory;
import com.example.demo.mapper.DirectoryMapper;
import com.example.demo.service.IDirectoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zwf
 * @since 2018-09-21
 */
@Service
public class DirectoryServiceImpl extends ServiceImpl<DirectoryMapper, Directory> implements IDirectoryService {

    @Autowired
    private DirectoryMapper directoryMapper;
    @Autowired
    private IDirectoryService iDirectoryService;

    @Override
    public Integer directoryMax(Map map){
        EntityWrapper ew = new EntityWrapper<Directory>();
        ew.setSqlSelect("IFNUll(max(sort),0)");
        Long i = (Long)iDirectoryService.selectObj(ew);
        //Integer integer = directoryMapper.directoryMax(map);
        return i.intValue();
    }
}
