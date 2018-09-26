package com.example.demo;

import com.example.demo.test.Demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@MapperScan("com.example.demo.mapper")
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DemoApplication.class, args);

        Timer timer = new Timer();
        timer.schedule(new MyTask() ,0,1000*60*60*6);

    }
}

// 定时器
class MyTask extends TimerTask {

    public void run(){
        Demo demo = new Demo();
        demo.demo();
    }
}