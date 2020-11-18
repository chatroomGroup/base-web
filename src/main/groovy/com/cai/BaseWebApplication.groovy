package com.cai

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
//@MapperScan("com.cai.web.*")
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
class BaseWebApplication {

    static void main(String[] args) {
        SpringApplication.run(BaseWebApplication, args)
    }

}
