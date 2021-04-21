package com.epam.training.ticketservice.commands.screening;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.ScreeningServiceInterface;

import java.time.LocalDateTime;

public class DeleteScreeningCommand implements Command {

    ScreeningServiceInterface screeningService;
    String movieTitle;
    String roomName;
    LocalDateTime startOfScreening;

    public DeleteScreeningCommand(ScreeningServiceInterface screeningService,
                                  String movieTitle,
                                  String roomName,
                                  LocalDateTime startOfScreening) {
        this.screeningService = screeningService;
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.startOfScreening = startOfScreening;
    }

    @Override
    public String execute() {
        try {
            screeningService.deleteScreening(movieTitle, roomName, startOfScreening);
            return "";
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
    }
}
