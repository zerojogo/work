package com.example.demo.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class Generator {

    public static void main(String[] args){

        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎 , 默认 Velocity
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("E:\\mywork");
        gc.setAuthor("zwf");
        gc.setFileOverride(true); // 是否覆盖同名文件
        gc.setActiveRecord(true); // 不需要 ActiveRecord 特性的请改为false
        gc.setEnableCache(false); // XML 二级缓存
        gc.setBaseResultMap(true); // XML ResultMap
        gc.setBaseColumnList(false);// XML columList

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert(){
           @Override
           public DbColumnType processTypeConvert(String fieldType){
               return super.processTypeConvert(fieldType);
           }
        });
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("admin");
        dsc.setUrl("jdbc:mysql://47.52.30.216:3306/my_novel?useUnicode=true&characterEncoding=utf8");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
      //  strategy.setTablePrefix(new String[] {"user_"}); // 此处可修改位表的前缀
        strategy.setNaming(NamingStrategy.nochange);
        strategy.setInclude(new String[] {"novel","directory","new_html"});

        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.example.demo");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        mpg.execute();


    }

}
