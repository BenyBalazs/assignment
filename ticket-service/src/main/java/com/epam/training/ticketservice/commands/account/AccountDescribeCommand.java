package com.epam.training.ticketservice.commands.account;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.AccountDescribeInterface;

import java.time.format.DateTimeFormatter;

public class AccountDescribeCommand implements Command {

    AccountDescribeInterface accountDescribeService;
    BookingStringBuilder bookingStringBuilder;

    public AccountDescribeCommand(AccountDescribeInterface accountDescribeService,
                                  DateTimeFormatter dateTimeFormatter) {
        this.accountDescribeService = accountDescribeService;
        this.bookingStringBuilder = new BookingStringBuilder(dateTimeFormatter);
    }

    @Override
    public String execute() {

        try {
            User user = accountDescribeService.getUser();
            //TODO LEFOGLALT JEGYEK LISTÁZÁSA
            if (User.Role.USER.equals(user.getRole())) {
                return "Signed in with account" + " '" + user.getUsername() + "'" + "\n"
                        + bookingStringBuilder.buildBookingString(user.getTickets());
            } else {
                return "Signed in with privileged account" + " '" + user.getUsername() + "'" + "\n"
                        + bookingStringBuilder.buildBookingString(user.getTickets());
            }
        } catch (UserNotLoggedInException e) {
            return e.getMessage();
        }
    }

}