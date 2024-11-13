package com.kpi.routetracker.utils.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringBeans {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //    @Bean
    //    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    //        return configuration.getAuthenticationManager();
    //    }
}
