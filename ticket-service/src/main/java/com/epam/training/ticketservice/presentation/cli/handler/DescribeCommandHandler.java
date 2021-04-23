package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.commands.account.AccountDescribeCommand;
import com.epam.training.ticketservice.service.interfaces.AccountDescribeInterface;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.format.DateTimeFormatter;

@ShellComponent
public class DescribeCommandHandler {

    private AccountDescribeInterface accountDescribeService;
    private DateTimeFormatter dateTimeFormatter;

    public DescribeCommandHandler(AccountDescribeInterface accountDescribeService,
                                  DateTimeFormatter dateTimeFormatter) {
        this.accountDescribeService = accountDescribeService;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @ShellMethod(value = "Describes the currently logged in account.", key = "describe account")
    public String describeAccount() {
        AccountDescribeCommand accountDescribeCommand =
                new AccountDescribeCommand(accountDescribeService, dateTimeFormatter);

        return accountDescribeCommand.execute();
    }
}
