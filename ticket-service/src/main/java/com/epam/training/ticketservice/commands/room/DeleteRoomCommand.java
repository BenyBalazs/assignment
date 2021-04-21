package com.epam.training.ticketservice.commands.room;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.RoomServiceInterface;

public class DeleteRoomCommand implements Command {

    private final RoomServiceInterface roomService;
    private final String roomName;

    public DeleteRoomCommand(RoomServiceInterface roomService, String roomName) {
        this.roomService = roomService;
        this.roomName = roomName;
    }

    @Override
    public String execute() {
        try {
            roomService.deleteRoom(roomName);
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }

        return null;
    }
}
