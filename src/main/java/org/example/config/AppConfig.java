package org.example.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
    public Clock appClock() {
        return Clock.systemUTC();
    }
}
