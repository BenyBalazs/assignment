package com.epam.training.ticketservice.utils;

import com.epam.training.ticketservice.data.entity.Ticket;
import lombok.Data;

@Data
public class TicketActionResultPair {

    private BookingActionResult bookingActionResult;
    private Ticket ticket;

    public boolean isFailed() {
        return !bookingActionResult.isSuccess();
    }
}
