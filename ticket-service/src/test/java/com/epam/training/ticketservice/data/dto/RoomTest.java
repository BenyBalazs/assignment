package com.epam.training.ticketservice.data.dto;

import com.epam.training.ticketservice.data.dao.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RoomTest {

    private Room room;

    @BeforeEach
    public void setUp() {
        room = new Room("ballada", null, 0, 0, null);
    }

    @Test
    public void testToStringShouldReturnFormattedString() {
        assertThat(room.toString(), equalTo("Room ballada with 0 seats, 0 rows and 0 columns"));
    }

}
