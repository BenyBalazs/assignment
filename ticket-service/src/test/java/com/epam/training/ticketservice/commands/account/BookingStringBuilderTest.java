package com.epam.training.ticketservice.commands.account;

import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.dao.Seat;
import com.epam.training.ticketservice.data.dao.Ticket;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.presentation.cli.configuration.CliConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(classes = CliConfiguration.class)
public class BookingStringBuilderTest {

    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    private BookingStringBuilder underTest;

    @BeforeEach
    public void setUp() {
        underTest = new BookingStringBuilder(dateTimeFormatter);
    }

    @Test
    public void testBuildBookingStringShouldReturnEmptyStringWhenNoTicketsAreBooked() {
        assertThat(underTest.buildBookingString(new ArrayList<>()), equalTo("You have not booked any tickets yet"));
    }

    @Test
    public void testBuildBookingStringShouldReturnFormattedTickets() {
        Room room = new Room("Pedersoli", new ArrayList<>(), new ArrayList<>());
        Movie movie = new Movie("Spirited Away", "anime", 88, new ArrayList<>());
        Screening screening = new Screening(1, movie, room, LocalDateTime.parse("2021-04-24 00:44", dateTimeFormatter));
        Seat seat = new Seat(1, 1, 1, room, new ArrayList<>());
        Ticket ticket = new Ticket(1,1500, seat, new User(), screening);

        assertThat(underTest.buildBookingString(List.of(ticket)), equalTo("Seats (1,1) on Spirited Away in room Pedersoli starting at 2021-04-24 00:44 for 1500 HUF"));
    }

    @Test
    public void testBuildBookingStringShouldReturnOneLineOfFormattedStringWithTheCorrectPriceAndAllTheBookedSeatsWhenTheUserHasBookingsOnlyInOneScreening() {
        Room room = new Room("Pedersoli", new ArrayList<>(), new ArrayList<>());
        Movie movie = new Movie("Spirited Away", "anime", 88, new ArrayList<>());
        Screening screening = new Screening(1, movie, room, LocalDateTime.parse("2021-04-24 00:44", dateTimeFormatter));
        Seat seat = new Seat(1, 1, 1, room, new ArrayList<>());
        Seat seat1 = new Seat(1, 2, 1, room, new ArrayList<>());
        Ticket ticket = new Ticket(1,1500, seat, new User(), screening);
        Ticket ticket1 = new Ticket(2, 1500, seat1, new User(), screening);

        assertThat(underTest.buildBookingString(List.of(ticket,ticket1)), equalTo("Seats (1,1), (2,1) on Spirited Away in room Pedersoli starting at 2021-04-24 00:44 for 3000 HUF"));
    }

    @Test
    public void testBuildBookingStringShouldReturnTwoLineOfFormattedStringWithTheCorrectPriceAndAllTheBookedSeatsWhenTheUserHasBookingsInMultipleScreening() {
        Room room = new Room("Pedersoli", new ArrayList<>(), new ArrayList<>());
        Movie movie = new Movie("Spirited Away", "anime", 88, new ArrayList<>());
        Screening screening = new Screening(1, movie, room, LocalDateTime.parse("2021-04-24 00:44", dateTimeFormatter));
        Screening screening1 = new Screening(2, movie, room, LocalDateTime.parse("2021-04-23 00:44", dateTimeFormatter));
        Seat seat = new Seat(1, 1, 1, room, new ArrayList<>());
        Seat seat1 = new Seat(1, 2, 1, room, new ArrayList<>());
        Ticket ticket = new Ticket(1,1500, seat, new User(), screening);
        Ticket ticket1 = new Ticket(2, 1500, seat1, new User(), screening1);

        assertThat(underTest.buildBookingString(List.of(ticket1,ticket)), equalTo("Seats (2,1) on Spirited Away in room Pedersoli starting at 2021-04-23 00:44 for 1500 HUF\nSeats (1,1) on Spirited Away in room Pedersoli starting at 2021-04-24 00:44 for 1500 HUF"));
    }
}
