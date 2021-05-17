package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Seat;

import java.util.List;

public interface SeatServiceInterface {

    List<Seat> createSeats(Room room, int cols, int rows);
}
