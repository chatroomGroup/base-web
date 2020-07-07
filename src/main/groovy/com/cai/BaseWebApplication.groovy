package com.cai

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class BaseWebApplication {

    static void main(String[] args) {
        SpringApplication.run(BaseWebApplication, args)
    }

}
