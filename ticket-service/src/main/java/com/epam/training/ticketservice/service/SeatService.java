package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Seat;
import com.epam.training.ticketservice.data.repository.SeatRepository;
import com.epam.training.ticketservice.service.interfaces.SeatServiceInterface;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class SeatService implements SeatServiceInterface {

    private SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> createSeats(Room room, int rows, int cols) {

        List<Seat> seats = new LinkedList<>();

        for (int i = 1; i < rows + 1; i++) {
            for (int j = 1; j < cols + 1; j++) {
                Seat seatToAdd = new Seat();
                seatToAdd.setColPosition(i);
                seatToAdd.setRowPosition(j);
                seatToAdd.setRoom(room);
                seats.add(seatToAdd);
            }
        }

        return seats;

    }

    public void deleteSeats(List<Seat> seats) {
        for (var seat: seats) {
            seat.setRoom(null);
            seatRepository.save(seat);
            seatRepository.delete(seat);
        }
    }
}
