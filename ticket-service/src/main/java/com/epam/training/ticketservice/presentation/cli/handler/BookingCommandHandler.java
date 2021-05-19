package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.presentation.cli.utils.CallBookingCommand;
import com.epam.training.ticketservice.presentation.cli.utils.SeatStringBuilder;
import com.epam.training.ticketservice.utils.BookingActionResult;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class BookingCommandHandler {

    private final CallBookingCommand callBookingCommand;
    private final SeatStringBuilder seatStringBuilder;

    public BookingCommandHandler(CallBookingCommand callBookingCommand, SeatStringBuilder seatStringBuilder) {
        this.callBookingCommand = callBookingCommand;
        this.seatStringBuilder = seatStringBuilder;
    }

    @ShellMethod(value = "This method is used to book seats to screenings.", key = "book")
    public String bookASeat(String title, String roomName, String startOfScreening, String seats) {
        try {
            BookingActionResult actionResult =
                    callBookingCommand.callBookCommand(title, roomName, startOfScreening, seats, true);
            if ("Taken".equals(actionResult.getMessage())) {
                return "Seat " + actionResult.getSeatIntPair().toString() + " is already taken";
            }
            if ("NoSeat".equals(actionResult.getMessage())) {
                return "Seat " + actionResult.getSeatIntPair().toString() + " does not exist in this room";
            }
            return "Seats booked: " + seatStringBuilder.buildSeatString(callBookingCommand.getSeatsToBook())
                    + "; the price for this booking is " + actionResult.getPrice() + " HUF";
        } catch (UserNotLoggedInException e) {
            return e.getMessage();
        }

    }

}

