package com.epam.training.ticketservice.presentation.cli.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class CliConfiguration {

    @Bean
    public String notSignInMassage() {
        return "You are not signed in";
    }

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

}
