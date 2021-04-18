package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.commands.RegisterCommand;
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
        RegisterCommand registerCommand = new RegisterCommand(registrationService, username, password);

        return registerCommand.execute();
    }
}
