package com.epam.training.ticketservice.data.dto;

import com.epam.training.ticketservice.data.dao.Room;
import org.junit.jupiter.api.BeforeEach;

public class RoomTest {

    private Room room;

    @BeforeEach
    public void setUp() {
        room = new Room("Debrecen", null, null);
    }

}
