package com.contactsservice.config;

import feign.codec.ErrorDecoder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.internal.Logger;

@Configuration
public class ContactConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.WARNING;
    }

    @Bean
    public ErrorDecoder getFeignErrorDecoder() {
        return new FeignErrorDecoder();
    }

}
