package com.epam.training.ticketservice.data.dto;

import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ScreeningTest {

    Screening screening;
    private DateTimeFormatter dateTimeFormatter;

    @BeforeEach
    public void setUp() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        screening = new Screening(1,
                new Movie("asdasd", "asdasd", 120, null),
                new Room("ballada", null, 0,0, null),
                LocalDateTime.parse("2021-04-22 16:00", dateTimeFormatter), LocalDateTime.parse("2021-04-22 18:00", dateTimeFormatter));
    }

    @Test
    public void testToStringShouldReturnFormattedString() {
        assertThat(screening.toString(), equalTo("asdasd (asdasd, 120 minutes), screened in room ballada, at 2021-04-22 16:00"));
    }

}
