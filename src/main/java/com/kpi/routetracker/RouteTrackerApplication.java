package com.kpi.routetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RouteTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouteTrackerApplication.class, args);
    }
}
