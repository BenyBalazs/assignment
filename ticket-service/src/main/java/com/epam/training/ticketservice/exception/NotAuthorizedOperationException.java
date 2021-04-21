package com.epam.training.ticketservice.exception;

public class NotAuthorizedOperationException extends Exception {

    public NotAuthorizedOperationException() {
        super("This is and admin command");
    }
}
