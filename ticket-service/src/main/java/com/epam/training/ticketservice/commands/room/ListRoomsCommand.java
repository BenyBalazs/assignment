package com.epam.training.ticketservice.commands.room;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.utils.Lister;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.service.interfaces.RoomServiceInterface;

public class ListRoomsCommand implements Command {

    private final Lister<Room> lister;

    public ListRoomsCommand(RoomServiceInterface roomService) {
        lister = new Lister<>("There are no rooms at the moment", roomService.getAllRooms());
    }

    @Override
    public String execute() {
        return lister.list();
    }
}
