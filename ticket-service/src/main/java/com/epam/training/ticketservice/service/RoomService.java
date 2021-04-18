package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.SeatRepository;
import com.epam.training.ticketservice.service.interfaces.RoomServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService implements RoomServiceInterface {

    private RoomRepository roomRepository;
    private SeatService seatService;

    public RoomService(RoomRepository roomRepository, SeatService seatService) {
        this.roomRepository = roomRepository;
        this.seatService = seatService;
    }

    @Override
    public boolean createRoom(String name, int columns, int rows) {

        Room roomToCreate = new Room();
        roomToCreate.setRoomName(name);
        roomRepository.save(roomToCreate);
        roomToCreate.setSeats(seatService.createSeats(columns,rows));
        roomRepository.save(roomToCreate);

        return true;
    }

    @Override
    public boolean modifyRoomSeats(String name, int columns, int rows) {

        Room roomToModify = roomRepository.findById(name).orElse(null);

        if (roomToModify == null) {
            return false;
        }

        roomToModify.setSeats(seatService.createSeats(columns, rows));

        return true;
    }

    @Override
    public boolean deleteRoom(String name) {
        Room roomToDelete = roomRepository.findById(name).orElse(null);

        if(roomToDelete == null) {
            return false;
        }

        roomRepository.delete(roomToDelete);

        return true;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
