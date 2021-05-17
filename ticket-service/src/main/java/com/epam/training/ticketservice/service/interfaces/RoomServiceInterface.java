package com.epam.training.ticketservice.service.interfaces;


import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;

import java.util.List;

public interface RoomServiceInterface {

    boolean createRoom(String name, int columns, int rows)
            throws UserNotLoggedInException, NotAuthorizedOperationException;

    boolean modifyRoomSeats(String name, int columns, int rows)
            throws UserNotLoggedInException, NotAuthorizedOperationException;

    boolean deleteRoom(String name) throws UserNotLoggedInException, NotAuthorizedOperationException;

    List<Room> getAllRooms();
}
