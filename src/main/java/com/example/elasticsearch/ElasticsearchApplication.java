package com.example.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

@SpringBootApplication
public class ElasticsearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
        LOGGER.info("======项目启动成功！======");
    }
}
