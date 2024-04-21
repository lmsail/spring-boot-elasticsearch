package com.example.elasticsearch.listener;

import com.example.elasticsearch.migration.UserAccountEsMigration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 应用启动时执行
 *
 * @author 特工007
 * @date 2022/5/5 12:08 PM
 */
@Component
public class StartListener implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("SpringBoot 程序运行时立即被触发调用....");
        UserAccountEsMigration.indexInit(); // 创建 user_account 索引
    }
}
