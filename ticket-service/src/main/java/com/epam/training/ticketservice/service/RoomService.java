package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.RoomServiceInterface;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoomService implements RoomServiceInterface {

    private RoomRepository roomRepository;
    private SeatService seatService;
    private AuthorizationService authorizationService;

    public RoomService(RoomRepository roomRepository,
                       SeatService seatService,
                       AuthorizationService authorizationService) {
        this.roomRepository = roomRepository;
        this.seatService = seatService;
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean createRoom(String name, int columns, int rows) throws UserNotLoggedInException,
            NotAuthorizedOperationException {

        authorizationService.userHasRoles(User.Role.ADMIN);

        if (roomRepository.findById(name).isPresent()) {
            return false;
        }

        Room roomToCreate = new Room();
        roomToCreate.setRoomName(name);
        roomRepository.save(roomToCreate);
        roomToCreate.setSeats(seatService.createSeats(columns,rows));
        roomRepository.save(roomToCreate);

        return true;
    }

    @Override
    public boolean modifyRoomSeats(String name, int columns, int rows) throws UserNotLoggedInException,
            NotAuthorizedOperationException {

        authorizationService.userHasRoles(User.Role.ADMIN);

        Room roomToModify = roomRepository.findById(name).orElse(null);

        if (roomToModify == null) {
            return false;
        }

        roomToModify.setSeats(seatService.createSeats(columns, rows));
        roomRepository.save(roomToModify);

        return true;
    }

    @Override
    public boolean deleteRoom(String name) throws UserNotLoggedInException, NotAuthorizedOperationException {

        authorizationService.userHasRoles(User.Role.ADMIN);

        Room roomToDelete = roomRepository.findById(name).orElse(null);

        if (roomToDelete == null) {
            return false;
        }

        roomRepository.delete(roomToDelete);

        return true;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    private List<User.Role> acceptedRoles(User.Role... roles) {
        return Arrays.asList(roles.clone());
    }
}
