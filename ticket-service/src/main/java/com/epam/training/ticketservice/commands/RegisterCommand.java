package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.service.interfaces.RegistrationInterface;

public class RegisterCommand implements Command {

    RegistrationInterface registrationService;
    String username;
    String password;

    public RegisterCommand(RegistrationInterface registrationService, String username, String password) {
        this.registrationService = registrationService;
        this.username = username;
        this.password = password;
    }

    @Override
    public String execute() {
        if (registrationService.register(new User(username, password, User.Role.USER))) {
            return "Sign up was successful";
        }
        else {
            return "Username is taken";
        }
    }
}
