package com.example.demo.test;


import ch.qos.logback.core.encoder.EchoEncoder;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.demo.entity.Directory;
import com.example.demo.entity.New_html;
import com.example.demo.entity.Novel;
import com.example.demo.service.IDirectoryService;
import com.example.demo.service.INew_htmlService;
import com.example.demo.service.INovelService;
import com.example.demo.utils.CommonUtil;
import com.example.demo.utils.IdWorkerUtils;
import com.sun.javafx.collections.MappingChange;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import jdk.nashorn.internal.runtime.regexp.joni.constants.EncloseType;
import org.jsoup.Jsoup;
import org.jsoup.select.Evaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.lang.model.util.Elements;
import javax.swing.text.Document;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Component
public class Demo  {

    @Autowired
    private IDirectoryService iDirectoryService;
    @Autowired
    private INovelService iNovelService;
    @Autowired
    private INew_htmlService iNew_htmlService;
    private static Demo demo ;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        demo = this;
        demo.iNovelService = this.iNovelService;
        demo.iDirectoryService=this.iDirectoryService;
        demo.iNew_htmlService = this.iNew_htmlService;
        // 初使化时将已静态化的testService实例化
    }

    @Transactional
    public void demo (){
        try {
            String strUrl = "http://www.ouoou.com";
            String stringBuffer = readHtml(strUrl);

            String str1 = "<div class="+'"'+"nav"+'"'+">"+'\n'+"<ul>";
            int index1 = stringBuffer.toString().indexOf(str1);

            String str2 = "</li>"+'\n'+"</ul>"+'\n'+"</div>";
            int index2 = stringBuffer.toString().indexOf(str2);

            // 截取分类栏上的所有信息
            String strLiList = stringBuffer.toString().substring(index1+23,index2);
            //         System.out.println(strLiList);
            //          System.out.println("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

            String[] strLi = strLiList.split("\n");
            for (int i = 1; /*i<strLi.length - 1*/ i<2; i++){

                // 截取单个分类信息
                String str = strLi[i];
                Map<String,Object> typeMap = type(str);
                List<Novel> novelList = new ArrayList<Novel>();
                List<Directory> directoryList = new ArrayList<>();
                List<Map<String,Object>> createHtmlList = new ArrayList<Map<String, Object>>();
                for (Map.Entry<String,Object> vo : typeMap.entrySet()){
                    vo.getKey();
                    vo.getValue();
                    String strRead = "";
                    try {
                        // 截取每个分类信息里面的所有信息
                        strRead = readHtml((String)vo.getValue());
                    }catch (Exception e){
                        e.printStackTrace();
                        continue;
                    }
                    String[] strings = strRead.split("</li><li>");
                    for (int j = 1; /*j<strings.length-1*/ j<3 ;j++ ){
                        String string = strings[j];
                        String reg = "/"+'"';
                        String novelUrl = string.substring(9,string.indexOf(reg));
                        String novelTitle = string.substring(string.indexOf("target="+'"'+"_blank"+'"'+">")+16,string.indexOf("</a>"));
                        String novelName = string.substring(string.indexOf("</a>")+4);
                        //          System.out.println(string);
                        //          System.out.println(novelUrl+'-'+novelTitle+'-'+novelName);
                        //          System.out.println("2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");

                        Map novelTitleMap = new HashMap();
                        novelTitleMap.put("novel_title",novelTitle);
                        List<Novel> novelTitleLists = demo.iNovelService.selectByMap(novelTitleMap);
                        Long novalId ;
                        if (CollectionUtils.isEmpty(novelTitleLists)){
                            novalId = IdWorkerUtils.getInstance().randomUUID();
                            Novel novel = new Novel();
                            novel.setNovel_id(novalId);
                            novel.setType_name(vo.getKey());
                            novel.setType_url((String)vo.getValue());
                            novel.setNovel_title(novelTitle);
                            novel.setNovel_name(novelName);
                            novel.setNovel_url(novelUrl);
                            novel.setCrt_date(new Date());
                            novelList.add(novel);
                        }else {
                            novalId = novelTitleLists.get(0).getNovel_id();
                        }

                        String directoryStr = "";
                        try {
                           directoryStr = readHtml("http://www.ouoou.com"+novelUrl+"/");
                        }catch (Exception e){
                            e.printStackTrace();
                            continue;
                        }

                        // 章节对应页面
                        List<Map<String,Object>> directorys = directory(directoryStr,novelTitle);
                        Map map = new HashMap();
                        map.put("novel_title",novelTitle);
                        int sort = demo.iDirectoryService.directoryMax(map); // 获取最大章节的顺序
                    //    for (Map<String, Object> directoryMap : directorys){
                            for (int k=0; k<directorys.size(); k++){
                            //    for (Map.Entry<String,Object> entry : directoryMap.entrySet()){
                                    for (Map.Entry<String,Object> entry : directorys.get(k).entrySet()){
                                        entry.getKey();
                                        entry.getValue();

                                        String chapterStr = "";
                                        try {
                                            chapterStr = readHtml("http://www.ouoou.com/"+entry.getKey());
                                        }catch (Exception e){
                                            e.printStackTrace();

                                            Directory directory = new Directory();
                                            directory.setDirectory_id(IdWorkerUtils.getInstance().randomUUID());
                                            directory.setNovel_id(novalId);
                                            directory.setSort(sort++);
                                            directory.setNovel_title(novelTitle);
                                            directoryList.add(directory);
                                            continue;
                                        }

                                        String novelDirectoryContent = chapter(chapterStr);
                                        //       System.out.println(novelDirectoryContent);
                                        //       System.out.println("5555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555");

                                        Directory directory = new Directory();
                                        directory.setDirectory_id(IdWorkerUtils.getInstance().randomUUID());
                                        directory.setNovel_id(novalId);
                                        directory.setSort(sort++);
                                        directory.setNovel_title(novelTitle);
                                        directory.setNovel_directory((String)entry.getValue());
                                        directory.setNovel_directory_url((String)entry.getKey());
                                        directory.setNovel_directory_content(novelDirectoryContent);
                                        directory.setCrt_date(new Date());
                                        directoryList.add(directory);

                                        // 封装生成页面需要的数据
                                        Map createHtmlMap = new HashMap();
                                        createHtmlMap.put("type_name",vo.getKey());
                                        createHtmlMap.put("type_url",(String)vo.getValue());
                                        createHtmlMap.put("novel_name",novelName);
                                        createHtmlMap.put("novel_url",novelUrl);
                                        createHtmlMap.put("novel_title",novelTitle);
                                        createHtmlMap.put("novel_directory",(String)entry.getValue());
                                        createHtmlMap.put("novel_directory_url",(String)entry.getKey());
                                        createHtmlMap.put("novel_directory_content",novelDirectoryContent);
                                        createHtmlList.add(createHtmlMap);
                                    }
                            //    }
                            }
                     //   }
                    }
                }
                demo.iNovelService.insertBatch(novelList);
                demo.iDirectoryService.insertBatch(directoryList);
                createHtml(createHtmlList);

                System.out.println("第一个"+i+"执行完成");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 获取页面所有信息
    public String readHtml (String strUrl) throws Exception{
        //  String strUrl = "http://ouoou.com/";
        URL url = new URL(strUrl);
        URLConnection urlConnection = url.openConnection();

        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line ;

        while ((line = bufferedReader.readLine()) != null){
            stringBuffer.append(line,0,line.length());
            stringBuffer.append('\n');
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        //    System.out.print(stringBuffer.toString());

        return stringBuffer.toString();
    }

    // 截取单个分类信息
    public Map<String,Object> type (String url) throws Exception{

        int index3 = url.indexOf("href=");
        int index4 = url.indexOf('"'+">");
        int index5 = url.indexOf("</a>");
        String typeUrl = url.substring(index3+6,index4);
        String typeName = url.substring(index4+2,index5);
        // key 小说类型   value 小说类型对应的链接
        Map<String,Object> typeMap = new HashMap();
        typeMap.put(typeName,typeUrl);
        //     System.out.println(typeName+'-'+typeUrl);
        //     System.out.println("3333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");

        return typeMap;
    }

    // 截取每个分类信息里面的所有信息
    public String typeAll(String typeStr) throws Exception{
        String str3 = "<div class="+'"'+"list"+'"'+">"+'\n'+"<ul>";
        int index6 = typeStr.indexOf(str3);
        String str4 = "</li></ul>"+'\n'+"<div ";
        int index7 = typeStr.indexOf(str4);
        typeStr.substring(index6+24,index7);
        //    System.out.println( typeStr.substring(index6+24,index7));
        //    System.out.println("4444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444");
        String[] strings = typeStr.substring(index6+28,index7).split("</li><li>");
        return typeStr.substring(index6+24,index7);
    }

    // 截取所有的章节目录
    public List<Map<String,Object>> directory(String directory,String novelTitle) throws Exception{
        String line = directory.substring(directory.indexOf("<dd><a"),directory.indexOf("</a></dd>"+'\n'+"</dl>"));
        String[] directorys = line.split("</a></dd>");
        List<Map<String,Object>> directoryList = new ArrayList<>();

        Map map = new HashMap();
        map.put("novel_title",novelTitle);
        int integer = demo.iDirectoryService.directoryMax(map);

        // 重新获取上一次没有获取到的信息
        EntityWrapper ew = new EntityWrapper();
        ew.setEntity(new New_html());
        List<New_html> newHtmlList = demo.iNew_htmlService.selectList(ew.eq("novel_title",novelTitle).eq("novel_directory",null));
        for (New_html newHtml : newHtmlList){
            String str = directorys[newHtml.getSort()];
            Map<String, Object> directoryMap = new HashMap<>();
            String novelDirectoryUrl = str.substring(str.indexOf("href")+6,str.indexOf("title")-2);
            String novelDirectory = str.substring(str.indexOf('"'+">")+2);
            directoryMap.put(novelDirectoryUrl,novelDirectory);
            directoryList.add(directoryMap);
        }


        for (int n = integer ; n<directorys.length ; n++){
            String str = directorys[n];
            Map<String,Object> directoryMap = new HashMap<>();
            String novelDirectoryUrl = str.substring(str.indexOf("href")+6,str.indexOf("title")-2);
            String novelDirectory = str.substring(str.indexOf('"'+">")+2);
            //   directoryMap.put(str.substring(str.indexOf("href")+6,str.indexOf("title")-2),str.substring(str.indexOf('"'+">")+2));
            directoryMap.put(novelDirectoryUrl,novelDirectory);
            directoryList.add(directoryMap);
            //    System.out.println(novelDirectoryUrl+'-'+novelDirectory);
        }
        return directoryList;

    }

    // 截取章节内容
    public String chapter(String chapterStr) throws Exception{
        String novelDirectoryContent = chapterStr.substring(chapterStr.indexOf("<!--章节内容开始-->")+13,chapterStr.indexOf("<!--章节内容结束-->"));
        return novelDirectoryContent;
    }


    // 生成页面
    public void createHtml(List<Map<String,Object>> createHtmlList){
        try {
            Configuration configuration = new Configuration();
            //    configuration.setDirectoryForTemplateLoading(new File("E:\\MyProject\\src\\main\\resources\\templates"));
            configuration.setDirectoryForTemplateLoading(new File("E:\\mywork"));
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setDefaultEncoding("UTF-8");
            for (Map map : createHtmlList){
                // 获取或创建一个模板页面
                Template template = configuration.getTemplate("template.html");

              /*  // 页面数据
                Map<String, Object> paramMap = new HashMap<String, Object>();*/

                // 生成八位随机不重复数 作为页面名称
                String s= UUID.randomUUID().toString();
                s =  s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
                String url = s.substring(0, 12);
                //    System.out.println(s.substring(0, 12));

                Writer writer = new OutputStreamWriter(new FileOutputStream("E:\\mywork\\template\\"+url+".html"),"UTF-8");
                template.process(map,writer);

                System.out.println("生成成功");

                IdWorkerUtils.getInstance().randomUUID();
                New_html new_html = new New_html();
                new_html.setNew_html_id(IdWorkerUtils.getInstance().randomUUID());

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
