package com.epam.training.ticketservice.commands.room;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.RoomServiceInterface;

public class CreateRoomCommand implements Command {

    private final RoomServiceInterface roomService;
    private final String roomName;
    private final int cols;
    private final int rows;

    public CreateRoomCommand(RoomServiceInterface roomService, String roomName, int cols, int rows) {
        this.roomService = roomService;
        this.roomName = roomName;
        this.cols = cols;
        this.rows = rows;
    }

    @Override
    public String execute() {

        try {
            if (roomService.createRoom(roomName, cols, rows)) {
                return "";
            } else {
                return "Room already exists";
            }
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
    }
}
