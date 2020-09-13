package com.redbyte.wigen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wangwq
 */
@SpringBootApplication
public class Spiders {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(Spiders.class);
        application.run(args);
    }
}
