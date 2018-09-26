package com.example.demo.service.impl;

import com.example.demo.entity.Novel;
import com.example.demo.mapper.NovelMapper;
import com.example.demo.service.INovelService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zwf
 * @since 2018-09-21
 */
@Service
public class NovelServiceImpl extends ServiceImpl<NovelMapper, Novel> implements INovelService {

}
