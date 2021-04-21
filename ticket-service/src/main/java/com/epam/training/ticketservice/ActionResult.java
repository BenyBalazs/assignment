package com.epam.training.ticketservice;

public class ActionResult {

    private String message;
    private boolean success;

    public ActionResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
