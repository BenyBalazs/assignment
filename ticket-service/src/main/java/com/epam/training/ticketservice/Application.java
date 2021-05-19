package com.epam.training.ticketservice;

import com.epam.training.ticketservice.data.entity.User;
import com.epam.training.ticketservice.service.BasePriceService;
import com.epam.training.ticketservice.service.RegistrationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.NoSuchElementException;

@SpringBootApplication(scanBasePackages = "com.epam.training.ticketservice")
public class Application {

    private final RegistrationService registrationService;
    private final BasePriceService basePriceService;

    public Application(RegistrationService registrationService, BasePriceService basePriceService) {
        this.registrationService = registrationService;
        this.basePriceService = basePriceService;
    }

    @PostConstruct
    public void createAdminAccount() {
        registrationService.register(new User("admin", "admin", User.Role.ADMIN));
        try {
            basePriceService.getBasePrice();
        } catch (NoSuchElementException t) {
            basePriceService.updateBasePrice(1500);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
