package com.epam.training.ticketservice.exception;

public class UserAlreadyLoggedInException extends Exception {

    public UserAlreadyLoggedInException() {
        super("You are already logged in");
    }
}
