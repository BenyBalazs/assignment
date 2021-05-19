package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.ScreeningServiceInterface;
import com.epam.training.ticketservice.utils.ActionResult;
import com.epam.training.ticketservice.utils.Lister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ShellComponent
public class ScreeningCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger("ScreeningCommandHandler.class");
    private final ScreeningServiceInterface screeningService;
    private final DateTimeFormatter dateTimeFormatter;

    public ScreeningCommandHandler(ScreeningServiceInterface screeningService,
                                   DateTimeFormatter dateTimeFormatter) {
        this.screeningService = screeningService;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @ShellMethod(value = "This method is used to create Screenings", key = "create screening")
    public String createScreening(String title, String roomName, String timeOfScreening) {
        LocalDateTime start = LocalDateTime.parse(timeOfScreening, dateTimeFormatter);

        try {
            ActionResult actionResult = screeningService.createScreening(title, roomName, start);

            if (actionResult.getMessage().equals("Overlapping")) {
                return "There is an overlapping screening";
            } else if (actionResult.getMessage().equals("BrakePeriod")) {
                return "This would start in the break period after another screening in this room";
            }
            return null;
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "This method is used to delete Screenings", key = "delete screening")
    public String deleteScreening(String title, String roomName, String timeOfScreening) {
        LocalDateTime start = LocalDateTime.parse(timeOfScreening, dateTimeFormatter);

        try {
            screeningService.deleteScreening(title, roomName, start);
            return null;
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "This method is used to list all screenings", key = "list screenings")
    public String listScreenings() {
        Lister<Screening> screeningLister =
                new Lister<>("There are no screenings", screeningService.getAllScreening());

        return screeningLister.list();
    }
}
