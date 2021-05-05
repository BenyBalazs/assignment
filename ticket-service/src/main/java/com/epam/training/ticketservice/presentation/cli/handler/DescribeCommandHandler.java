package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.presentation.cli.utils.BookingStringBuilder;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.AccountDescribeInterface;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class DescribeCommandHandler {

    private AccountDescribeInterface accountDescribeService;
    private BookingStringBuilder bookingStringBuilder;

    public DescribeCommandHandler(AccountDescribeInterface accountDescribeService,
                                  BookingStringBuilder bookingStringBuilder) {
        this.accountDescribeService = accountDescribeService;
        this.bookingStringBuilder = bookingStringBuilder;
    }

    @ShellMethod(value = "Describes the currently logged in account.", key = "describe account")
    public String describeAccount() {

        try {
            User user = accountDescribeService.getUser();
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
