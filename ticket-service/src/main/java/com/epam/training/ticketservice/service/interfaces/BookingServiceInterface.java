package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.ActionResult;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingServiceInterface {

    ActionResult bookSeat(String movieTitle, String roomName, LocalDateTime startOfScreening, List<String> seatsToBook)
            throws UserNotLoggedInException;
}
