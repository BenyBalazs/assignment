package com.epam.training.ticketservice.commands.screening;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.exception.OverlappingScreeningException;
import com.epam.training.ticketservice.exception.ScreeningInTheBrakePeriodException;
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
            if (screeningService.createScreening(movieTitle, roomName, startOfScreening)) {
                return "";
            } else {
                return "Movie or room does not exist.";
            }

        } catch (OverlappingScreeningException e) {
            return "There is an overlapping screening";
        } catch (ScreeningInTheBrakePeriodException e) {
            return "This would start in the break period after another screening in this room";
        }
    }
}
