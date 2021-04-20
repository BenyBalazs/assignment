package com.epam.training.ticketservice.service;

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

    public List<Seat> createSeats(int cols, int rows) {

        List<Seat> seats = new LinkedList<>();

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Seat seatToAdd = new Seat();
                seatToAdd.setColPosition(i);
                seatToAdd.setRowPosition(j);
                seatRepository.save(seatToAdd);
                seats.add(seatToAdd);
            }
        }

        return seats;

    }
}
