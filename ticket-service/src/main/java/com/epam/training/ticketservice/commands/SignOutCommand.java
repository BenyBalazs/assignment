package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.service.interfaces.SignOutInterface;

public class SignOutCommand implements Command{

    private SignOutInterface signOutService;

    public SignOutCommand(SignOutInterface signOutService) {
        this.signOutService = signOutService;
    }

    @Override
    public String execute() {
        signOutService.singOut();

        return "";
    }
}
