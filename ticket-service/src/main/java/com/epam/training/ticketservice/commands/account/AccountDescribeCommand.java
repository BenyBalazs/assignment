package com.epam.training.ticketservice.commands.account;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.AccountDescribeInterface;

public class AccountDescribeCommand implements Command {

    AccountDescribeInterface accountDescribeService;

    public AccountDescribeCommand(AccountDescribeInterface accountDescribeService) {
        this.accountDescribeService = accountDescribeService;
    }

    @Override
    public String execute() {

        try {
            User user = accountDescribeService.getUser();
            //TODO LEFOGLALT JEGYEK LISTÁZÁSA
            if (User.Role.USER.equals(user.getRole())) {
                return "Signed in with account" + " '" + user.getUsername() + "'";
            } else {
                return "Signed in with privileged account" + " '" + user.getUsername() + "'";
            }
        } catch (UserNotLoggedInException e) {
            return "You are not signed in";
        }
    }
}
