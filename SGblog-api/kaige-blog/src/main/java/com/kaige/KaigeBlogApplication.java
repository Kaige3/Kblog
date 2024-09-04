package com.kaige;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.kaige.mapper")
@EnableScheduling
@EnableSwagger2
public class KaigeBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(KaigeBlogApplication.class,args);
    }
}
