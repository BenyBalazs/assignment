package com.epam.training.ticketservice.service.interfaces;


import com.epam.training.ticketservice.data.dao.Room;

import java.util.List;

public interface RoomServiceInterface {

    boolean createRoom(String name, int columns, int rows);

    boolean modifyRoomSeats(String name, int columns, int rows);

    boolean deleteRoom(String name);

    List<Room> getAllRooms();
}
