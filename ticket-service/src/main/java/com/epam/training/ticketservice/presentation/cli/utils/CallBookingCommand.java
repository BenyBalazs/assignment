package com.epam.training.ticketservice.presentation.cli.utils;

import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.BookingServiceInterface;
import com.epam.training.ticketservice.utils.BookingActionResult;
import com.epam.training.ticketservice.utils.SeatIntPair;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CallBookingCommand {

    private final BookingServiceInterface bookingService;
    private final DateTimeFormatter dateTimeFormatter;

    private List<SeatIntPair> seatsToBook;

    public CallBookingCommand(BookingServiceInterface bookingService, DateTimeFormatter dateTimeFormatter) {
        this.bookingService = bookingService;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public BookingActionResult callBookCommand(String title, String roomName, String startOfScreening, String seats,
                                  boolean persist) throws UserNotLoggedInException {

        SeatIntPairBuilder seatIntPairBuilder = new SeatIntPairBuilder();
        LocalDateTime parsedStartOfScreening = LocalDateTime.parse(startOfScreening, dateTimeFormatter);
        seatsToBook = null;
        seatsToBook = seatIntPairBuilder.buildList(seats);

        return bookingService
                    .bookSeat(title, roomName, parsedStartOfScreening, seatsToBook, persist);

    }

    public List<SeatIntPair> getSeatsToBook() {
        return seatsToBook;
    }
}
