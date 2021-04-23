package com.epam.training.ticketservice.commands.booking;

import com.epam.training.ticketservice.BookingActionResult;
import com.epam.training.ticketservice.SeatIntPair;
import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.BookingServiceInterface;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BookASeatCommand implements Command {

    BookingServiceInterface bookingService;
    String movieTitle;
    String roomName;
    LocalDateTime startOfScreening;
    List<SeatIntPair> seatsToBook;

    public BookASeatCommand(BookingServiceInterface bookingService,
                            String movieTitle,
                            String roomName,
                            LocalDateTime startOfScreening,
                            List<SeatIntPair> seatsToBook) {
        this.bookingService = bookingService;
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.startOfScreening = startOfScreening;
        this.seatsToBook = seatsToBook;
    }

    @Override
    public String execute() {
        try {
            BookingActionResult actionResult = bookingService
                    .bookSeat(movieTitle, roomName,startOfScreening, seatsToBook);
            if ("Taken".equals(actionResult.getMessage())) {
                return "Seat" + actionResult.getSeatIntPair().toString() + " is already taken";
            }
            if ("NoSeat".equals(actionResult.getMessage())) {
                return "Seat" + actionResult.getSeatIntPair().toString() + " does not exist in this room";
            }
            return "Seats booked: " + buildSeatString(seatsToBook)
                    + "; the price for this booking is " + actionResult.getPrice() + "HUF";
        } catch (UserNotLoggedInException e) {
            return e.getMessage();
        }
    }

    private String buildSeatString(List<SeatIntPair> seats) {
        return seats.stream().map(SeatIntPair::toString).collect(Collectors.joining(","));
    }
}
