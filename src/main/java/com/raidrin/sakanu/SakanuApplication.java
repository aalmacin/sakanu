package com.raidrin.sakanu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SakanuApplication {

    public static void main(String[] args) {
        SpringApplication.run(SakanuApplication.class, args);
    }

}
