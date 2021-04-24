package com.epam.training.ticketservice.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BookingActionResult extends ActionResult {

    private SeatIntPair seatIntPair;
    private int price;

    public BookingActionResult(String message, boolean success) {
        super(message, success);
    }

    public BookingActionResult(String message, boolean success, SeatIntPair seatIntPair) {
        super(message, success);
        this.seatIntPair = seatIntPair;
    }

    public BookingActionResult(String message, boolean success, int price) {
        super(message, success);
        this.price = price;
    }

}
