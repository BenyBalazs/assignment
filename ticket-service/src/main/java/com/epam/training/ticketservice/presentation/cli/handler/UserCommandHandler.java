package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.commands.LogInCommand;
import com.epam.training.ticketservice.service.LogInService;
import com.epam.training.ticketservice.service.interfaces.LogInInterface;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UserCommandHandler {

    LogInCommand logInCommand;
    LogInInterface logInService;

    public UserCommandHandler(LogInInterface logInService) {
        this.logInService = logInService;
    }

    @ShellMethod(value = "Sings in the user", key = "sign in")
    public String singIn(String username, String password) {
        logInCommand = new LogInCommand(logInService, username, password);

        return logInCommand.execute();
    }

    @ShellMethod(value = "Sings in privileged users.", key = "sign in privileged")
    public String singInPrivileged(String username, String password) {
        logInCommand = new LogInCommand(logInService, username, password);

        return logInCommand.execute();
    }

    @ShellMethod(value = "Sings out the loged in user.", key = "sign out")
    public String signOut() {
        return "Signed out";
    }

    @ShellMethod(value = "Describes the currently logged in account.", key = "describe account")
    public String describeAccount() {
        return "describe";
    }
}
