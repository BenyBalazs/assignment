package com.epam.training.ticketservice.commands.room;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.RoomServiceInterface;

public class ModifyRoomCommand implements Command {

    RoomServiceInterface roomService;
    String roomName;
    int cols;
    int rows;

    public ModifyRoomCommand(RoomServiceInterface roomService, String roomName, int cols, int rows) {
        this.roomService = roomService;
        this.roomName = roomName;
        this.cols = cols;
        this.rows = rows;
    }

    @Override
    public String execute() {
        try {
            roomService.modifyRoomSeats(roomName, cols, rows);
            return "";
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
    }
}
