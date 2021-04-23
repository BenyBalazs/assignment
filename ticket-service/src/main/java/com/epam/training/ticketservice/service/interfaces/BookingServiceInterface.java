package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.utils.BookingActionResult;
import com.epam.training.ticketservice.utils.SeatIntPair;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingServiceInterface {

    BookingActionResult bookSeat(String movieTitle, String roomName, LocalDateTime startOfScreening,
                                 List<SeatIntPair> seatsToBook)
            throws UserNotLoggedInException;
}
