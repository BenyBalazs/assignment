package com.epam.training.ticketservice.exception;

public class UserNotLoggedInException extends Throwable {
    public UserNotLoggedInException() {
        super("You are not signed in");
    }
}
