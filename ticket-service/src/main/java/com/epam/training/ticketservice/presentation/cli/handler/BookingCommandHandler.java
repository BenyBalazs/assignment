package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.utils.SeatIntPair;
import com.epam.training.ticketservice.commands.booking.BookASeatCommand;
import com.epam.training.ticketservice.service.interfaces.BookingServiceInterface;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class BookingCommandHandler {

    BookingServiceInterface bookingService;
    DateTimeFormatter dateTimeFormatter;

    public BookingCommandHandler(BookingServiceInterface bookingService, DateTimeFormatter dateTimeFormatter) {
        this.bookingService = bookingService;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @ShellMethod(value = "This method is used to book seats to screenings.", key = "book")
    public String bookASeat(String title, String roomName, String startOfScreening, String seats) {
        SeatIntPairBuilder builder = new SeatIntPairBuilder();
        
        BookASeatCommand bookASeatCommand 
                = new BookASeatCommand(bookingService, title, roomName,
                LocalDateTime.parse(startOfScreening, dateTimeFormatter), builder.buildList(seats));

        return bookASeatCommand.execute();
    }
}

