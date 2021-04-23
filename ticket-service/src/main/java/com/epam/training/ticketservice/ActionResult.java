package com.epam.training.ticketservice;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class ActionResult {

    private String message;
    private boolean success;

    public ActionResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
