package com.example.demo.generator;


import com.example.demo.service.IDirectoryService;
import com.example.demo.service.INovelService;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class CreateHtml {

    @Autowired
    private IDirectoryService iDirectoryService;
    @Autowired
    private INovelService iNovelService;
    private static CreateHtml createHtml;
    @PostConstruct
    public void init(){
        createHtml = this;
        createHtml.iNovelService = this.iNovelService;
        createHtml.iDirectoryService = this.iDirectoryService;
    }

    public static void main(String[] args){
        try {
            Configuration configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(new File("E:\\MyProject\\src\\main\\resources\\templates"));
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setDefaultEncoding("UTF-8");
            // 获取或创建一个模板页面
            Template template = configuration.getTemplate("template.html");

            // 页面数据封装
            Map<String, Object> paramMap = new HashMap<String, Object>();

            Writer writer = new OutputStreamWriter(new FileOutputStream("success.html"),"UTF-8");
            template.process(paramMap,writer);

            System.out.println("生成成功");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
