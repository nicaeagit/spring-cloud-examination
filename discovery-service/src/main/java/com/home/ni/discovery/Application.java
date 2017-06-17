package com.home.ni.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by Administrator on 2017/6/17.
 */
@SpringBootApplication
@EnableEurekaServer

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
