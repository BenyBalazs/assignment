package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.commands.room.CreateRoomCommand;
import com.epam.training.ticketservice.commands.room.DeleteRoomCommand;
import com.epam.training.ticketservice.commands.room.ListRoomsCommand;
import com.epam.training.ticketservice.commands.room.ModifyRoomCommand;
import com.epam.training.ticketservice.service.interfaces.RoomServiceInterface;
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
        CreateRoomCommand command = new CreateRoomCommand(roomService, roomName, rows, cols);
        return command.execute();
    }

    @ShellMethod(value = "This method is used to delete rooms", key = "delete room")
    public String deleteRoom(String roomName) {
        DeleteRoomCommand command = new DeleteRoomCommand(roomService, roomName);
        return command.execute();
    }

    @ShellMethod(value = "This method is used to modify room seats", key = "update room")
    public String modifyRoom(String roomName, int rows, int cols) {
        ModifyRoomCommand command = new ModifyRoomCommand(roomService, roomName, rows, cols);
        return command.execute();
    }

    @ShellMethod(value = "This method is used to list all rooms", key = "list rooms")
    public String listRooms() {
        ListRoomsCommand command = new ListRoomsCommand(roomService);
        return command.execute();
    }
}
