package com.cydeo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TicketingProjectOrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectOrmApplication.class, args);
    }


//we added extra class with mapper and  security dependencies to use them we need to add bean

    @Bean
    public ModelMapper mapper() {

        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
