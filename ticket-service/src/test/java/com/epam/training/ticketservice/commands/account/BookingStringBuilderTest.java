package com.epam.training.ticketservice.commands.account;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.data.entity.Seat;
import com.epam.training.ticketservice.data.entity.Ticket;
import com.epam.training.ticketservice.data.entity.User;
import com.epam.training.ticketservice.presentation.cli.configuration.CliConfiguration;
import com.epam.training.ticketservice.presentation.cli.utils.BookingStringBuilder;
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

    private static final Room roomOfScreening  = new Room("Pedersoli", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    private static final Movie movie = new Movie("Spirited Away", "anime", 88, new ArrayList<>(), new ArrayList<>());
    private static final User basicUser = new User("bela", "123", User.Role.USER, new ArrayList<>());
    private static final User adminUser = new User("bela", "123", User.Role.ADMIN, new ArrayList<>());

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
        Screening screening = new Screening(1, movie, roomOfScreening, LocalDateTime.parse("2021-04-24 00:44", dateTimeFormatter));
        Seat seat = new Seat(1, 1, 1, roomOfScreening, new ArrayList<>());
        Ticket ticket = new Ticket(1,1500, seat, new User(), screening);

        assertThat(underTest.buildBookingString(List.of(ticket)), equalTo("Your previous bookings are\nSeats (1,1) on Spirited Away in room Pedersoli starting at 2021-04-24 00:44 for 1500 HUF"));
    }

    @Test
    public void testBuildBookingStringShouldReturnOneLineOfFormattedStringWithTheCorrectPriceAndAllTheBookedSeatsWhenTheUserHasBookingsOnlyInOneScreening() {
        Screening screening = new Screening(1, movie, roomOfScreening, LocalDateTime.parse("2021-04-24 00:44", dateTimeFormatter));
        Seat seat = new Seat(1, 1, 1, roomOfScreening, new ArrayList<>());
        Seat seat1 = new Seat(1, 2, 1, roomOfScreening, new ArrayList<>());
        Ticket ticket = new Ticket(1,1500, seat, new User(), screening);
        Ticket ticket1 = new Ticket(2, 1500, seat1, new User(), screening);

        assertThat(underTest.buildBookingString(List.of(ticket,ticket1)), equalTo("Your previous bookings are\nSeats (1,1), (2,1) on Spirited Away in room Pedersoli starting at 2021-04-24 00:44 for 3000 HUF"));
    }

    @Test
    public void testBuildBookingStringShouldReturnTwoLineOfFormattedStringWithTheCorrectPriceAndAllTheBookedSeatsWhenTheUserHasBookingsInMultipleScreening() {

        Screening screening = new Screening(1, movie, roomOfScreening, LocalDateTime.parse("2021-04-24 00:44", dateTimeFormatter));
        Screening screening1 = new Screening(2, movie, roomOfScreening, LocalDateTime.parse("2021-04-23 00:44", dateTimeFormatter));
        Seat seat = new Seat(1, 1, 1, roomOfScreening, new ArrayList<>());
        Seat seat1 = new Seat(1, 2, 1, roomOfScreening, new ArrayList<>());
        Ticket ticket = new Ticket(1,1500, seat, new User(), screening);
        Ticket ticket1 = new Ticket(2, 1500, seat1, new User(), screening1);

        assertThat(underTest.buildBookingString(List.of(ticket1,ticket)), equalTo("Your previous bookings are\nSeats (2,1) on Spirited Away in room Pedersoli starting at 2021-04-23 00:44 for 1500 HUF\nSeats (1,1) on Spirited Away in room Pedersoli starting at 2021-04-24 00:44 for 1500 HUF"));
    }
}
