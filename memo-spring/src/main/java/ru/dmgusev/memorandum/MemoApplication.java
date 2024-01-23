package ru.dmgusev.memorandum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MemoApplication /*extends SpringBootServletInitializer*/ {

    /*
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MemoApplication.class);
    }
    */

    public static void main(String[] args) {
        SpringApplication.run(MemoApplication.class, args);
    }
}
