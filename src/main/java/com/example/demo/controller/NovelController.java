package com.example.demo.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.demo.entity.Novel;
import com.example.demo.service.INovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zwf
 * @since 2018-09-21
 */
@Controller
@RequestMapping("/novel")
public class NovelController {

    @Autowired
    private INovelService iNovelService;

    @RequestMapping(value = "/novelList" , method = RequestMethod.POST)
    @ResponseBody
    public List<Novel> selectNovel(){
        EntityWrapper ew = new EntityWrapper<Novel>();
        List<Novel> novelList = iNovelService.selectList(ew);
        return novelList;
    }

}
