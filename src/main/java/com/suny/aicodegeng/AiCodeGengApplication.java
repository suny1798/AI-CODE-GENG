package com.suny.aicodegeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class AiCodeGengApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCodeGengApplication.class, args);
    }

}
