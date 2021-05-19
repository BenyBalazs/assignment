package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.utils.BookingActionResult;
import com.epam.training.ticketservice.utils.SeatIntPair;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowPriceServiceInterface {

     BookingActionResult showPrice(String movieTitle,
                                        String roomName,
                                        LocalDateTime startOfScreening,
                                        List<SeatIntPair> seatsToBook);
}
