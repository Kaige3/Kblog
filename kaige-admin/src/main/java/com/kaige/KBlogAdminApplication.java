package com.kaige;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kaige.mapper")
public class KBlogAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(KBlogAdminApplication.class,args);
    }
}