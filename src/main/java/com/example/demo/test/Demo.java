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
import java.net.SocketTimeoutException;
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

            System.out.println("开始解析");
            String[] strLi = strLiList.split("\n");
            for (int i = 1; i<strLi.length - 1 ; i++){

                // 截取单个分类信息
                String str = strLi[i];
                Map<String,Object> typeMap = type(str);
                List<Novel> novelList = new ArrayList<Novel>();
                List<Directory> directoryList = new ArrayList<>();
                List<Map<String,Object>> createHtmlList = new ArrayList<Map<String, Object>>();
                for (Map.Entry<String,Object> vo : typeMap.entrySet()){
                    vo.getKey();   // ouoou  typeName 不准确
                    vo.getValue();  // ouoot typeUrl
                    String strRead = "";
                    try {
                        // 截取每个分类信息里面的所有信息
                        strRead = readHtml((String)vo.getValue());
                    }catch (Exception e){
                        e.printStackTrace();
                        continue;
                    }
                    String[] strings = strRead.split("</li><li>");
                    for (int j = 1; j<strings.length-1 ;j++ ){
                        String string = strings[j];
                        String reg = "/"+'"';
                        String oldNovelUrl = string.substring(9,string.indexOf(reg));  // 原网站的小说主页链接


                        String novelTitle = string.substring(string.indexOf("target="+'"'+"_blank"+'"'+">")+16,string.indexOf("</a>"));
                        String novelName = string.substring(string.indexOf("</a>")+4);
                        //          System.out.println(string);
                        //          System.out.println(novelUrl+'-'+novelTitle+'-'+novelName);
                        //          System.out.println("2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");

                        String directoryStr = "";
                        try {
                            directoryStr = readHtml("http://www.ouoou.com"+oldNovelUrl+"/");
                        }catch (Exception e){
                            e.printStackTrace();
                            continue;
                        }

                        // 抓取最新的数据
                        Map map = new HashMap();
                        map.put("novel_title",novelTitle);
                        int sort = demo.iDirectoryService.directoryMax(map); // 获取最大章节的顺序

                        List<Novel> novelList1 = demo.iNovelService.selectByMap(map);
                        String novelUrl ;
                        if (CollectionUtils.isEmpty(novelList1)){
                            // 生成新的小说主页链接
                            String s= UUID.randomUUID().toString();
                            s =  s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
                            novelUrl = s.substring(0, 6);
                        }else {
                            novelUrl = novelList1.get(0).getNovel_url();
                        }

                        // 章节对应页面
                        Map<String,Object> directoryMap= directory(directoryStr,novelTitle,sort);

                        // 截取单篇小说的简介
                        String novelIntro = directoryStr.substring(directoryStr.indexOf("intro")+8,directoryStr.indexOf("</p>"+'\n'+"</div>"+'\n'+"</div>"+'\n'+"</div>")+4);

                        // 更新之前没有抓取到的数据
                        EntityWrapper ew = new EntityWrapper();
                        ew.setEntity(new New_html());
                        List<New_html> newHtmlList = demo.iNew_htmlService.selectList(ew);
                        List<Directory>  oldDirectorys = oldDirectory(directoryStr,novelTitle,newHtmlList);
                        if (!CollectionUtils.isEmpty(oldDirectorys)){
                            for (Directory directory : oldDirectorys){
                                String chapterStr = "";
                                try {
                                    chapterStr = readHtml("http://www.ouoou.com/"+directory.getNovel_directory_url());
                                }catch (Exception e){
                                    e.printStackTrace();
                                    continue;
                                }
                                String novelDirectoryContent = chapter(chapterStr);
                                directory.setDirectory_id(directory.getDirectory_id());
                                //    directory.setNovel_directory_content(novelDirectoryContent);
                                directory.setCrt_date(new Date());
                                directoryList.add(directory);

                                // 封装生成页面需要的数据
                                Map createHtmlMap = new HashMap();
                                createHtmlMap.put("type_name",(String) directoryMap.get("typeName"));
                                createHtmlMap.put("type_url",(String)vo.getValue());
                                createHtmlMap.put("novel_name",novelName);
                                createHtmlMap.put("novel_url",novelUrl);
                                createHtmlMap.put("novel_title",novelTitle);
                                createHtmlMap.put("novel_directory",directory.getNovel_directory());
                                createHtmlMap.put("novel_directory_url",directory.getNovel_directory_url());
                                createHtmlMap.put("novel_directory_content",novelDirectoryContent);
                                createHtmlList.add(createHtmlMap);
                                System.out.println("update");
                            }
                            demo.iDirectoryService.updateBatchById(directoryList);
                            createHtml(createHtmlList);
                        }



                        Map novelTitleMap = new HashMap();
                        novelTitleMap.put("novel_title",novelTitle);
                        List<Novel> novelTitleLists = demo.iNovelService.selectByMap(novelTitleMap);
                        Long novelId ;
                        if (CollectionUtils.isEmpty(novelTitleLists)){
                            novelId = IdWorkerUtils.getInstance().randomUUID();
                            Novel novel = new Novel();
                            novel.setNovel_id(novelId);
                            novel.setType_name((String) directoryMap.get("typeName"));
                            novel.setType_url((String)vo.getValue());
                            novel.setNovel_title(novelTitle);
                            novel.setNovel_name(novelName);
                            novel.setNovel_url(novelUrl);
                            novel.setNovel_intro(novelIntro);
                            novel.setCrt_date(new Date());
                            novelList.add(novel);

                            Map createNovelHtmlMap = new HashMap();
                            createNovelHtmlMap.put("novel_id",novelId);
                            createNovelHtmlMap.put("type_name",(String) directoryMap.get("typeName"));
                            createNovelHtmlMap.put("type_url",(String)vo.getValue());
                            createNovelHtmlMap.put("novel_title",novelTitle);
                            createNovelHtmlMap.put("novel_name",novelName);
                            createNovelHtmlMap.put("novel_url",novelUrl);
                            createNovelHtmlMap.put("novel_intro",novelIntro);
                            createNovelHtmlMap.put("crt_date",novel.getCrt_date());
                            createNovelHtml(createNovelHtmlMap);

                        }else {
                            novelId = novelTitleLists.get(0).getNovel_id();
                        }

                        List<Map<String,Object>> directorys = (ArrayList)directoryMap.get("directoryList");
                        if (!CollectionUtils.isEmpty(directorys)){
                            //    for (Map<String, Object> directoryMap : directorys){
                            for (int k=0; k<directorys.size(); k++){
                                //    for (Map.Entry<String,Object> entry : directoryMap.entrySet()){
                                for (Map.Entry<String,Object> entry : directorys.get(k).entrySet()){
                                    entry.getKey(); //  原章节url
                                    entry.getValue();

                                    String chapterStr = "";
                                    try {
                                        chapterStr = readHtml("http://www.ouoou.com/"+entry.getKey());
                                    }catch (SocketTimeoutException ste){
                                        ste.printStackTrace();
                                        Directory directory = new Directory();
                                        Long directoryId = IdWorkerUtils.getInstance().randomUUID();
                                        directory.setDirectory_id(directoryId);
                                        directory.setNovel_id(novelId);
                                        directory.setSort(sort++);
                                        directory.setNovel_title(novelTitle);
                                        directoryList.add(directory);

                                        New_html newHtml = new New_html();
                                        newHtml.setTry_catch_id(IdWorkerUtils.getInstance().randomUUID());
                                        newHtml.setDirectory_id(directoryId);
                                        newHtml.setSort(sort++);
                                        newHtml.setNovel_title(novelTitle);
                                        newHtml.setCrt_date(new Date());
                                        continue;
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();

                                        Directory directory = new Directory();
                                        Long directoryId = IdWorkerUtils.getInstance().randomUUID();
                                        directory.setDirectory_id(directoryId);
                                        directory.setNovel_id(novelId);
                                        directory.setSort(sort++);
                                        directory.setNovel_title(novelTitle);
                                        directoryList.add(directory);

                                        New_html newHtml = new New_html();
                                        newHtml.setTry_catch_id(IdWorkerUtils.getInstance().randomUUID());
                                        newHtml.setDirectory_id(directoryId);
                                        newHtml.setSort(sort++);
                                        newHtml.setNovel_title(novelTitle);
                                        newHtml.setCrt_date(new Date());
                                        continue;
                                    }

                                    String novelDirectoryContent = chapter(chapterStr);
                                    //       System.out.println(novelDirectoryContent);
                                    //       System.out.println("5555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555");

                                    // 生成八位随机不重复数 作为页面名称
                                    String sss= UUID.randomUUID().toString();
                                    sss =  sss.substring(0,8)+sss.substring(9,13)+sss.substring(14,18)+sss.substring(19,23)+sss.substring(24);
                                    String url = sss.substring(0, 12);
                                    //    System.out.println(s.substring(0, 12));

                                    Directory directory = new Directory();
                                    directory.setDirectory_id(IdWorkerUtils.getInstance().randomUUID());
                                    directory.setNovel_id(novelId);
                                    directory.setSort(sort++);
                                    directory.setNovel_title(novelTitle);
                                    directory.setNovel_directory((String)entry.getValue());
                                    directory.setNovel_directory_url(url+".html"); // 使用新生成的数
                                    //     directory.setNovel_directory_content(novelDirectoryContent);
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
                                    createHtmlMap.put("novel_directory_url",url);
                                    createHtmlMap.put("novel_directory_content",novelDirectoryContent);
                                    createHtmlList.add(createHtmlMap);
                                }
                                //    }
                            }
                            //   }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(novelList) && !CollectionUtils.isEmpty(directoryList) && !CollectionUtils.isEmpty(createHtmlList)){
                    demo.iNovelService.insertBatch(novelList);
                    System.out.println("类型"+i+"执行完成");
                }
                if (!CollectionUtils.isEmpty(directoryList)){
                    demo.iDirectoryService.insertBatch(directoryList);
                    System.out.println("正文执行成功");
                }
                if (!CollectionUtils.isEmpty(createHtmlList)){
                    createHtml(createHtmlList);
                    System.out.println("页面生成成功");
                }
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

        // java.net.SocketTimeoutException: Read timed out
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
        System.out.println("3333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");

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
    public Map<String,Object> directory(String directory,String novelTitle,Integer sort) throws Exception{
        String line = directory.substring(directory.indexOf("<dd><a"),directory.indexOf("</a></dd>"+'\n'+"</dl>"));
        String[] directorys = line.split("</a></dd>");
        List<Map<String,Object>> directoryList = new ArrayList<>();
        Map<String,Object> map = new HashMap<String, Object>();

     /*   Map map = new HashMap();
        map.put("novel_title",novelTitle);
        int integer = demo.iDirectoryService.directoryMax(map);*/
        String typeName = directory.substring(directory.indexOf("<p>类")+8,directory.indexOf("</p>"+'\n'+"<p>作"));
        if (sort != 0){
            sort = sort +1;
        }
        for (int n = sort ; n<directorys.length ; n++){
            String str = directorys[n];
            Map<String,Object> directoryMap = new HashMap<>();
            String novelDirectoryUrl = str.substring(str.indexOf("href")+6,str.indexOf("title")-2);
            String novelDirectory = str.substring(str.indexOf('"'+">")+2);
            //   directoryMap.put(str.substring(str.indexOf("href")+6,str.indexOf("title")-2),str.substring(str.indexOf('"'+">")+2));


            directoryMap.put(novelDirectoryUrl,novelDirectory);
            directoryList.add(directoryMap);
            map.put("typeName",typeName);
            map.put("directoryList",directoryList);
            //    System.out.println(novelDirectoryUrl+'-'+novelDirectory);
        }
        return map;

    }

    // 重新获取上一次没有获取到的信息
    public List<Directory> oldDirectory(String directory,String novelTitle,List<New_html> directoryList1) throws Exception{
        String line = directory.substring(directory.indexOf("<dd><a"),directory.indexOf("</a></dd>"+'\n'+"</dl>"));
        String[] directorys = line.split("</a></dd>");
        List<Directory> directoryList = new ArrayList<>();

       /* EntityWrapper ew = new EntityWrapper();
        ew.setEntity(new New_html());
        List<New_html> newHtmlList = demo.iNew_htmlService.selectList(ew.eq("novel_title",novelTitle).eq("novel_directory",null));*/
        if (!CollectionUtils.isEmpty(directoryList1)){
            for (New_html newHtml : directoryList1){
                String str = directorys[newHtml.getSort()];
                //    Map<String, Object> directoryMap = new HashMap<>();
                String novelDirectoryUrl = str.substring(str.indexOf("href")+6,str.indexOf("title")-2);
                String novelDirectory = str.substring(str.indexOf('"'+">")+2);
                //    directoryMap.put(novelDirectoryUrl,novelDirectory);
                Directory directory1 = new Directory();
                directory1.setDirectory_id(newHtml.getDirectory_id());
                directory1.setNovel_directory_url(novelDirectoryUrl);
                directory1.setNovel_directory(novelDirectory);
                directoryList.add(directory1);

                // 生成成功之后 , 删除new_html 记录的没有成功的记录
                demo.iNew_htmlService.deleteById(newHtml);
            }
        }
        return directoryList;
    }

    // 截取章节内容
    public String chapter(String chapterStr) throws Exception{
        String novelDirectoryContent = chapterStr.substring(chapterStr.indexOf("<!--章节内容开始-->")+13,chapterStr.indexOf("<!--章节内容结束-->"));
        return novelDirectoryContent;
    }


    // 生成章节页面
    public void createHtml(List<Map<String,Object>> createHtmlList){
        try {
            Configuration configuration = new Configuration();
            //    configuration.setDirectoryForTemplateLoading(new File("E:\\MyProject\\src\\main\\resources\\templates"));
            configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates/"));
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setDefaultEncoding("UTF-8");
            List<New_html> newHtmlList = new ArrayList<>();
            for (Map map : createHtmlList){
                // 获取或创建一个模板页面
                Template template = configuration.getTemplate("template.html");

              /*  // 页面数据
                Map<String, Object> paramMap = new HashMap<String, Object>();*/



                File file = new File("src/main/resources/templates/"+map.get("novel_url"));
                if (!file.exists() && !file.isDirectory()){
                    file.mkdir();
                }else {
                    Writer writer = new OutputStreamWriter(new FileOutputStream("src/main/resources/templates/"+map.get("novel_url")+"\\"+map.get("novel_directory_url")+".html"),"UTF-8");
                    template.process(map,writer);

                    System.out.println("生成成功");
                }

            }
            /*  demo.iNew_htmlService.insertBatch(newHtmlList);*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 生成主页页面
    public void createNovelHtml(Map<String,Object> createNovelHtmlMap){
        try {
            Configuration configuration = new Configuration();
            //    configuration.setDirectoryForTemplateLoading(new File("E:\\MyProject\\src\\main\\resources\\templates"));
            configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates/"));
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setDefaultEncoding("UTF-8");
            List<New_html> newHtmlList = new ArrayList<>();
            // 获取或创建一个模板页面
            Template template = configuration.getTemplate("index.html");


            File file = new File("src/main/resources/templates/"+createNovelHtmlMap.get("novel_url"));
            if (!file.exists() && !file.isDirectory()){
                file.mkdir();
                Writer writer = new OutputStreamWriter(new FileOutputStream("src/main/resources/templates/"+createNovelHtmlMap.get("novel_url")+"\\"+createNovelHtmlMap.get("novel_url")+".html"),"UTF-8");
                template.process(createNovelHtmlMap,writer);

                System.out.println("生成成功");
            }else {
                Writer writer = new OutputStreamWriter(new FileOutputStream("src/main/resources/templates/"+createNovelHtmlMap.get("novel_url")+"\\"+createNovelHtmlMap.get("novel_url")+".html"),"UTF-8");
                template.process(createNovelHtmlMap,writer);

                System.out.println("生成成功");
            }

            /*  demo.iNew_htmlService.insertBatch(newHtmlList);*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
