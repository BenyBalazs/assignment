package com.epam.training.ticketservice.commands.account;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import com.epam.training.ticketservice.service.interfaces.LogInInterface;

public class LogInCommand implements Command {

    LogInInterface logInService;
    String username;
    String password;

    public LogInCommand(LogInInterface logInService, String username, String password) {
        this.logInService = logInService;
        this.username = username;
        this.password = password;
    }

    @Override
    public String execute() {

        try {
            if (logInService.logIn(username, password)) {
                return "";
            }
            return "Login failed due to incorrect credentials.";
        } catch (UserAlreadyLoggedInException e) {
            return "Already Logged in.";
        } catch (NotAuthorizedOperationException e) {
            return "";
        }
    }

}
