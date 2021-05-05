package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.PrivilegedLogInService;
import com.epam.training.ticketservice.service.interfaces.SignOutInterface;
import com.epam.training.ticketservice.service.user.LogInService;
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
        try {
            if (logInService.logIn(username, password)) {
                return "";
            }
            return "Login failed due to incorrect credentials.";
        } catch (UserAlreadyLoggedInException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Sings in privileged users.", key = "sign in privileged")
    public String singInPrivileged(String username, String password) {
        try {
            if (privilegedLogInService.logIn(username,password)) {
                return "";
            }
            return "Login failed due to incorrect credentials.";
        } catch (UserAlreadyLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Sings out the logged in user.", key = "sign out")
    public String signOut() {
        try {
            signOutService.singOut();
            return "";
        } catch (UserNotLoggedInException e) {
            return e.getMessage();
        }
    }

}
