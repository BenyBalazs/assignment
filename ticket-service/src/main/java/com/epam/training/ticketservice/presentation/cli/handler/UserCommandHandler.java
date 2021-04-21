package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.commands.account.LogInCommand;
import com.epam.training.ticketservice.commands.account.PrivilegedLogInCommand;
import com.epam.training.ticketservice.commands.account.SignOutCommand;
import com.epam.training.ticketservice.service.LogInService;
import com.epam.training.ticketservice.service.PrivilegedLogInService;
import com.epam.training.ticketservice.service.interfaces.SignOutInterface;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UserCommandHandler {

    LogInService logInService;
    PrivilegedLogInService privilegedLogInService;
    SignOutInterface signOutService;

    public UserCommandHandler(LogInService logInService,
                              PrivilegedLogInService privilegedLogInService,
                              SignOutInterface signOutService) {
        this.logInService = logInService;
        this.privilegedLogInService = privilegedLogInService;
        this.signOutService = signOutService;
    }

    @ShellMethod(value = "Sings in the user", key = "sign in")
    public String singIn(String username, String password) {
        LogInCommand logInCommand = new LogInCommand(logInService, username, password);

        return logInCommand.execute();
    }

    @ShellMethod(value = "Sings in privileged users.", key = "sign in privileged")
    public String singInPrivileged(String username, String password) {
        PrivilegedLogInCommand privilegedLogInCommand =
                new PrivilegedLogInCommand(privilegedLogInService, username, password);

        return privilegedLogInCommand.execute();
    }

    @ShellMethod(value = "Sings out the logged in user.", key = "sign out")
    public String signOut() {
        SignOutCommand signOutCommand = new SignOutCommand(signOutService);

        return signOutCommand.execute();
    }

}
