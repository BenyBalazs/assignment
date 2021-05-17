package com.epam.training.ticketservice.data.dto;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Screening;
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
                new Room("ballada", null, null),
                LocalDateTime.parse("2021-04-22 16:00", dateTimeFormatter));
    }

    @Test
    public void testToStringShouldReturnFormattedString() {
        assertThat(screening.toString(), equalTo("asdasd (asdasd, 120 minutes), screened in room ballada, at 2021-04-22 16:00"));
    }

}
