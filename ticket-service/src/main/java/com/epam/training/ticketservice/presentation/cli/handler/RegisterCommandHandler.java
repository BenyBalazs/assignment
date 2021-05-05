package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.service.interfaces.RegistrationInterface;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class RegisterCommandHandler {

    private RegistrationInterface registrationService;

    public RegisterCommandHandler(RegistrationInterface registrationService) {
        this.registrationService = registrationService;
    }

    @ShellMethod(value = "Registers new User into the application.", key = "sign up")
    public String register(String username, String password) {
        if (registrationService.register(new User(username, password, User.Role.USER, null))) {
            return "Sign up was successful";
        } else {
            return "Username is taken";
        }
    }

}
