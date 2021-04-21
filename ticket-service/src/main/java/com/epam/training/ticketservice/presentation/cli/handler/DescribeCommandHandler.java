package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.commands.account.AccountDescribeCommand;
import com.epam.training.ticketservice.service.interfaces.AccountDescribeInterface;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class DescribeCommandHandler {

    private AccountDescribeInterface accountDescribeService;

    public DescribeCommandHandler(AccountDescribeInterface accountDescribeService) {
        this.accountDescribeService = accountDescribeService;
    }

    @ShellMethod(value = "Describes the currently logged in account.", key = "describe account")
    public String describeAccount() {
        AccountDescribeCommand accountDescribeCommand = new AccountDescribeCommand(accountDescribeService);

        return accountDescribeCommand.execute();
    }
}
