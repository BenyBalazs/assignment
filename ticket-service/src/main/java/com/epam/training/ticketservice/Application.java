package com.epam.training.ticketservice;

import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.service.RegistrationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = "com.epam.training.ticketservice")
public class Application {

    private final RegistrationService registrationService;

    public Application(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostConstruct
    public void createAdminAccount() {
        registrationService.register(new User("admin", "admin", User.Role.ADMIN));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
