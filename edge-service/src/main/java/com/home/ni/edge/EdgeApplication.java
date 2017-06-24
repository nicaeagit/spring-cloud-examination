package com.home.ni.edge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.RetryLoadBalancerInterceptor;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2017/6/17.
 */

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@EnableResourceServer
@EnableHystrix
public class EdgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdgeApplication.class, args);
    }

    @Configuration
    public static class RestSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
        }
    }


//    @Bean
//    UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer(SpringClientFactory springClientFactory) {
//        return template -> {
//            AccessTokenProviderChain accessTokenProviderChain = Stream
//                    .of(
//                            new AuthorizationCodeAccessTokenProvider(),
//                            new ImplicitAccessTokenProvider(),
//                            new ResourceOwnerPasswordAccessTokenProvider(),
//                            new ClientCredentialsAccessTokenProvider())
//                    .peek(tp -> tp.setRequestFactory(new RibbonClientHttpRequestFactory(springClientFactory)))
//                    .collect(Collectors.collectingAndThen(Collectors.toList(), AccessTokenProviderChain::new));
//            template.setAccessTokenProvider(accessTokenProviderChain);
//        };
//    }


    @Bean
    UserInfoRestTemplateCustomizer oauth2RestTemplateCustomizer(RetryLoadBalancerInterceptor interceptor) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(interceptor);
        return template -> {
            AccessTokenProviderChain accessTokenProviderChain = Stream
                    .of(new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
                            new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider())
                    .peek(tp -> tp.setInterceptors(interceptors))
                    .collect(Collectors.collectingAndThen(Collectors.toList(), AccessTokenProviderChain::new));
            template.setAccessTokenProvider(accessTokenProviderChain);
        };
    }
}
