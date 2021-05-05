package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.BookingServiceInterface;
import com.epam.training.ticketservice.utils.BookingActionResult;
import com.epam.training.ticketservice.utils.SeatIntPair;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        SeatIntPairBuilder seatIntPairBuilder = new SeatIntPairBuilder();
        LocalDateTime parsedStartOfScreening = LocalDateTime.parse(startOfScreening, dateTimeFormatter);
        List<SeatIntPair> seatsToBook = null;
        seatsToBook = seatIntPairBuilder.buildList(seats);
        System.out.println(seatsToBook);

        try {
            BookingActionResult actionResult = bookingService
                    .bookSeat(title, roomName, parsedStartOfScreening, seatsToBook);
            if ("Taken".equals(actionResult.getMessage())) {
                return "Seat" + actionResult.getSeatIntPair().toString() + " is already taken";
            }
            if ("NoSeat".equals(actionResult.getMessage())) {
                return "Seat" + actionResult.getSeatIntPair().toString() + " does not exist in this room";
            }
            return "Seats booked: " + buildSeatString(seatsToBook)
                    + "; the price for this booking is " + actionResult.getPrice() + " HUF";
        } catch (UserNotLoggedInException e) {
            return e.getMessage();
        }
    }

    private String buildSeatString(List<SeatIntPair> seats) {
        return seats.stream().map(SeatIntPair::toString).collect(Collectors.joining(","));
    }
}

