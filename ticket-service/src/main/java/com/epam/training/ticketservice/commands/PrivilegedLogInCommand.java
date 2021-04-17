package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.exception.NotAuthorizedLogInException;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import com.epam.training.ticketservice.service.interfaces.LogInInterface;

public class PrivilegedLogInCommand implements Command {

    LogInInterface privilegedLogInService;
    String username;
    String password;

    public PrivilegedLogInCommand(LogInInterface privilegedLogInService, String username, String password) {
        this.privilegedLogInService = privilegedLogInService;
        this.username = username;
        this.password = password;
    }

    @Override
    public String execute() {

        try {
            if (privilegedLogInService.logIn(username,password)) {
                return "";
            }
            return "Login failed due to incorrect credentials.";
        } catch (UserAlreadyLoggedInException e) {
            return "Already Logged in.";
        } catch (NotAuthorizedLogInException e) {
            return "You can not log in privileged with non administrator account.";
        }
    }
}
