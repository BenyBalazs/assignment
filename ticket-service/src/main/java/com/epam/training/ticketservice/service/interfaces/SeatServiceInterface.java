package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.data.dao.Seat;

import java.util.List;

public interface SeatServiceInterface {

    List<Seat> createSeats(int cols, int rows);
}
