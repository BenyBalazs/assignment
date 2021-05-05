package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.RoomServiceInterface;
import com.epam.training.ticketservice.utils.Lister;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class RoomCommandHandler {

    RoomServiceInterface roomService;

    public RoomCommandHandler(RoomServiceInterface roomService) {
        this.roomService = roomService;
    }

    @ShellMethod(value = "This method is used to create rooms", key = "create room")
    public String createRoom(String roomName, int rows, int cols) {
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

    @ShellMethod(value = "This method is used to delete rooms", key = "delete room")
    public String deleteRoom(String roomName) {
        try {
            roomService.deleteRoom(roomName);
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }

        return null;
    }

    @ShellMethod(value = "This method is used to modify room seats", key = "update room")
    public String modifyRoom(String roomName, int rows, int cols) {
        try {
            roomService.modifyRoomSeats(roomName, cols, rows);
            return "";
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "This method is used to list all rooms", key = "list rooms")
    public String listRooms() {
        Lister<Room> lister =
                new Lister<>("There are no rooms at the moment", roomService.getAllRooms());

        return lister.list();
    }
}
