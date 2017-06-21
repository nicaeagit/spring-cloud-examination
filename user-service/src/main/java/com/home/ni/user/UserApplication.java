package com.home.ni.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Created by Administrator on 2017/6/17.
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableResourceServer
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }



    @Controller
    @RequestMapping("/")
    public static class DummyController {

        @RequestMapping(method = RequestMethod.GET)
        @ResponseBody
        public String helloWorld(Principal principal) {
            return principal == null ? "Hello anonymous" : "Hello " + principal.getName();
        }

        @PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
        @RequestMapping(value = "secret", method = RequestMethod.GET)
        @ResponseBody
        public String helloSecret(Principal principal) {
            return principal == null ? "Hello anonymous" : "S3CR3T  - Hello " + principal.getName();
        }
    }
}
