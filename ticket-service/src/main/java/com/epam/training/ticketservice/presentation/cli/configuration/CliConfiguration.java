package com.epam.training.ticketservice.presentation.cli.configuration;

import com.epam.training.ticketservice.presentation.cli.utils.BookingStringBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.format.DateTimeFormatter;

@Configuration
public class CliConfiguration {

    @Bean
    public String notSignInMassage() {
        return "You are not signed in";
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    @Bean
    public BookingStringBuilder bookingStringBuilder(DateTimeFormatter dateTimeFormatter) {
        return new BookingStringBuilder(dateTimeFormatter);
    }
}
