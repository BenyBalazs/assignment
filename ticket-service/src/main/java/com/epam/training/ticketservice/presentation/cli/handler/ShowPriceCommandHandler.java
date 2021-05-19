package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.presentation.cli.utils.CallBookingCommand;
import com.epam.training.ticketservice.presentation.cli.utils.SeatStringBuilder;
import com.epam.training.ticketservice.utils.BookingActionResult;
import lombok.SneakyThrows;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShowPriceCommandHandler {

    CallBookingCommand callBookingCommand;
    SeatStringBuilder seatStringBuilder;

    public ShowPriceCommandHandler(CallBookingCommand callBookingCommand, SeatStringBuilder seatStringBuilder) {
        this.callBookingCommand = callBookingCommand;
        this.seatStringBuilder = seatStringBuilder;
    }

    @SneakyThrows
    @ShellMethod(value = "This method is used to show price for a given booking",
            key = "show price for")
    public String showPrice(String title, String roomName, String startOfScreening, String seats) {

        BookingActionResult actionResult =
                callBookingCommand.callBookCommand(title, roomName, startOfScreening, seats, false);
        if ("Taken".equals(actionResult.getMessage())) {
            return "Seat " + actionResult.getSeatIntPair().toString() + " is already taken";
        }
        if ("NoSeat".equals(actionResult.getMessage())) {
            return "Seat " + actionResult.getSeatIntPair().toString() + " does not exist in this room";
        }
        return "The price for this booking would be " + actionResult.getPrice() + " HUF";

    }
}
