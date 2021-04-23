package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.commands.screening.CreateScreeningCommand;
import com.epam.training.ticketservice.commands.screening.DeleteScreeningCommand;
import com.epam.training.ticketservice.commands.screening.ListScreeningCommand;
import com.epam.training.ticketservice.service.interfaces.ScreeningServiceInterface;
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
        logger.trace("the time is {}", start.format(dateTimeFormatter));
        CreateScreeningCommand command = new CreateScreeningCommand(screeningService, title, roomName, start);
        return command.execute();
    }

    @ShellMethod(value = "This method is used to delete Screenings", key = "delete screening")
    public String deleteScreening(String title, String roomName, String timeOfScreening) {
        LocalDateTime start = LocalDateTime.parse(timeOfScreening, dateTimeFormatter);
        DeleteScreeningCommand command = new DeleteScreeningCommand(screeningService, title, roomName, start);
        return command.execute();
    }

    @ShellMethod(value = "This method is used to list all screenings", key = "list screenings")
    public String listScreenings() {
        ListScreeningCommand command = new ListScreeningCommand(screeningService);
        return command.execute();
    }
}
