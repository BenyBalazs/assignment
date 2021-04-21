package com.epam.training.ticketservice.commands.screening;

import com.epam.training.ticketservice.ActionResult;
import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.ScreeningServiceInterface;

import java.time.LocalDateTime;

public class CreateScreeningCommand implements Command {

    ScreeningServiceInterface screeningService;
    String movieTitle;
    String roomName;
    LocalDateTime startOfScreening;

    @Override
    public String execute() {
        try {
            ActionResult actionResult = screeningService.createScreening(movieTitle, roomName, startOfScreening);

            if (actionResult.getMessage().equals("Overlapping")) {
                return "There is an overlapping screening";
            }
            else if (actionResult.getMessage().equals("BrakePeriod")) {
                return "This would start in the break period after another screening in this room";
            }
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
        return "";
    }
}