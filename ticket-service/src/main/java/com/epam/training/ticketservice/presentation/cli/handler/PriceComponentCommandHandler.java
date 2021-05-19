package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.PriceComponentService;
import com.epam.training.ticketservice.utils.ActionResult;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ShellComponent
public class PriceComponentCommandHandler {

    private final PriceComponentService priceComponentService;
    private final DateTimeFormatter dateTimeFormatter;

    public PriceComponentCommandHandler(PriceComponentService priceComponentService,
                                        DateTimeFormatter dateTimeFormatter) {
        this.priceComponentService = priceComponentService;
        this.dateTimeFormatter = dateTimeFormatter;
    }


    @ShellMethod(value = "This method is used to attach price component to screening",
            key = "attach price component to screening")
    public String attachComponentToScreening(String priceComponentName,
                                             String movieTitle, String roomName, String startOfScreening) {
        LocalDateTime start = LocalDateTime.parse(startOfScreening, dateTimeFormatter);

        ActionResult result;

        try {
            result = priceComponentService.attachPriceComponentToScreening(movieTitle,
                    roomName, start, priceComponentName);
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }

        if (result.getMessage().equals("NoScreening")) {
            return "No Screening was fount with these variables";
        }

        return "Price Component is attached to the screening";

    }


    @ShellMethod(value = "This method is used to attach price component to movie",
            key = "attach price component to movie")
    public String attachComponentToMovie(String componentName, String movieTitle) {
        ActionResult result;
        System.out.println(componentName);
        System.out.println(movieTitle);
        try {
            result = priceComponentService.attachPriceComponentToMovie(movieTitle, componentName);
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }

        if (result.getMessage().equals("NoMovie")) {
            return "Whe have no movies with this title";
        }

        return "Price Component is attached to the movie";
    }

    @ShellMethod(value = "This method is used to attach price component to room",
            key = "attach price component to room")
    public String attachComponentToRoom(String componentName, String roomName) {
        ActionResult result;

        try {
            result = priceComponentService.attachPriceComponentToRoom(roomName, componentName);
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }

        if (result.getMessage().equals("NoRoom")) {
            return "Whe have no room with this name";
        }

        return "Price Component is attached to the room";
    }

    @ShellMethod(value = "This method is used to create price component",
            key = "create price component")
    public String createPriceComponent(String name, int price) {

        boolean success = false;

        try {
            success = priceComponentService.createPriceComponent(name, price);
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }

        if (success) {
            return "Price Component created";
        }

        return "Price Component already exists!";
    }

}
