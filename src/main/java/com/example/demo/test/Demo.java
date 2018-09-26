package com.example.demo.test;


import com.example.demo.entity.Directory;
import com.example.demo.entity.Novel;
import com.example.demo.service.IDirectoryService;
import com.example.demo.service.INovelService;
import com.example.demo.utils.IdWorkerUtils;
import com.sun.javafx.collections.MappingChange;
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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Component
public class Demo  {

    @Autowired
    private IDirectoryService iDirectoryService;
    @Autowired
    private INovelService iNovelService;
    private static Demo demo ;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        demo = this;
        demo.iNovelService = this.iNovelService;
        demo.iDirectoryService=this.iDirectoryService;
        // 初使化时将已静态化的testService实例化
    }

    @Transactional
    public void demo (){
        try {
            String strUrl = "http://www.ouoou.com//ou_26680/21704506.html";
            String stringBuffer = readHtml(strUrl);

            String str1 = "<div class="+'"'+"nav"+'"'+">"+'\n'+"<ul>";
            int index1 = stringBuffer.toString().indexOf(str1);

            String str2 = "</li>"+'\n'+"</ul>"+'\n'+"</div>";
            int index2 = stringBuffer.toString().indexOf(str2);

            // 截取分类栏上的所有信息
            String strLiList = stringBuffer.toString().substring(index1+23,index2);
            System.out.println(strLiList);
            System.out.println("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

            String[] strLi = strLiList.split("\n");
            for (int i = 1; /*i<strLi.length - 1*/ i<2; i++){

                // 截取单个分类信息
                String str = strLi[i];
                Map<String,Object> typeMap = type(str);
                List<Novel> novelList = new ArrayList<Novel>();
                List<Directory> directoryList = new ArrayList<>();
                for (Map.Entry<String,Object> vo : typeMap.entrySet()){
                    vo.getKey();
                    vo.getValue();

                    // 截取每个分类信息里面的所有信息
                    String strRead = readHtml((String)vo.getValue());
                    String[] strings = strRead.split("</li><li>");
                    for (int j = 1; j<strings.length-1 ;j++ ){
                        String string = strings[j];
                        String reg = "/"+'"';
                        String novelUrl = string.substring(9,string.indexOf(reg));
                        String novelTitle = string.substring(string.indexOf("target="+'"'+"_blank"+'"'+">")+16,string.indexOf("</a>"));
                        String novelName = string.substring(string.indexOf("</a>")+4);
                        System.out.println(string);
                        System.out.println(novelUrl+'-'+novelTitle+'-'+novelName);
                        System.out.println("2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");

                        Map novelTitleMap = new HashMap();
                        novelTitleMap.put("novel_title",novelTitle);
                        List<Novel> novelTitleLists = demo.iNovelService.selectByMap(novelTitleMap);
                        Long novalId ;
                        if (CollectionUtils.isEmpty(novelTitleLists)){
                            novalId = IdWorkerUtils.getInstance().randomUUID();
                        }else {
                            novalId = novelTitleLists.get(0).getNovel_id();
                        }
                        Novel novel = new Novel();
                        novel.setNovel_id(novalId);
                        novel.setType_name(vo.getKey());
                        novel.setType_url((String)vo.getValue());
                        novel.setNovel_title(novelTitle);
                        novel.setNovel_name(novelName);
                        novel.setNovel_url(novelUrl);
                        novelList.add(novel);

                        String directoryStr = readHtml("http://www.ouoou.com"+novelUrl+"/");
                        // 章节对应页面
                        List<Map<String,Object>> directorys = directory(directoryStr,novelTitle);
                        for (Map<String, Object> directoryMap : directorys){

                            for (Map.Entry<String,Object> entry : directoryMap.entrySet()){
                                entry.getKey();
                                entry.getValue();
                                String chapterStr = readHtml("http://www.ouoou.com/"+entry.getKey());
                                String novelDirectoryContent = chapter(chapterStr);
                                System.out.println(novelDirectoryContent);
                                System.out.println("5555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555");

                                Directory directory = new Directory();
                                directory.setDirectory_id(IdWorkerUtils.getInstance().randomUUID());
                                directory.setNovel_id(novalId);
                                directory.setNovel_directory((String)entry.getValue());
                                directory.setNovel_directory_url((String)entry.getKey());
                                directory.setNovel_directory_content(novelDirectoryContent);
                                directoryList.add(directory);

                            }
                        }
                    }
                }
                demo.iNovelService.insertBatch(novelList);
                demo.iDirectoryService.insertBatch(directoryList);
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
        try {
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
        }catch (Exception e){
            e.printStackTrace();
        }
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
        System.out.println(typeName+'-'+typeUrl);
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
        System.out.println( typeStr.substring(index6+24,index7));
        System.out.println("4444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444");
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

        for (int n = integer ; n<directorys.length ; n++){
            String str = directorys[n];
            Map<String,Object> directoryMap = new HashMap<>();
            String novelDirectoryUrl = str.substring(str.indexOf("href")+6,str.indexOf("title")-2);
            String novelDirectory = str.substring(str.indexOf('"'+">")+2);
            //   directoryMap.put(str.substring(str.indexOf("href")+6,str.indexOf("title")-2),str.substring(str.indexOf('"'+">")+2));
            directoryMap.put(novelDirectoryUrl,novelDirectory);
            directoryList.add(directoryMap);
            System.out.println(novelDirectoryUrl+'-'+novelDirectory);
        }
        return directoryList;

    }

    // 截取章节内容
    public String chapter(String chapterStr) throws Exception{
        String novelDirectoryContent = chapterStr.substring(chapterStr.indexOf("<!--章节内容开始-->")+13,chapterStr.indexOf("<!--章节内容结束-->"));
        return novelDirectoryContent;
    }

}