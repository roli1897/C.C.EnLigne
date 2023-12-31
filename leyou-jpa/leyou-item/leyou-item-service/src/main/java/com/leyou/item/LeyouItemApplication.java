package com.leyou.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class LeyouItemApplication {
    public static void main(String[] args) {

        SpringApplication.run(LeyouItemApplication.class,args);
    }
}
