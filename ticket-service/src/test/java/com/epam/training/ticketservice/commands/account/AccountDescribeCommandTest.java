package com.epam.training.ticketservice.commands.account;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.data.entity.Seat;
import com.epam.training.ticketservice.data.entity.Ticket;
import com.epam.training.ticketservice.data.entity.User;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.presentation.cli.configuration.CliConfiguration;
import com.epam.training.ticketservice.presentation.cli.handler.DescribeCommandHandler;
import com.epam.training.ticketservice.presentation.cli.utils.BookingStringBuilder;
import com.epam.training.ticketservice.service.user.AccountDescribeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BookingStringBuilder.class, CliConfiguration.class})
public class AccountDescribeCommandTest {

    DescribeCommandHandler underTest;
    @Autowired
    BookingStringBuilder bookingStringBuilder;
    @Autowired
    DateTimeFormatter dateTimeFormatter;
    @MockBean
    AccountDescribeService accountDescribeService;

    @BeforeEach
    public void setUp() {
        accountDescribeService = Mockito.mock(AccountDescribeService.class);
        underTest = new DescribeCommandHandler(accountDescribeService, bookingStringBuilder);
    }

    @SneakyThrows
    @Test
    public void testExecuteShouldReturnNonAdminSignedInMassageAndNoBookingWhenTheUserIsLoggedInAndHasNoBookingsAndDoesNotHaveAdminAccess() {
        User user = new User("bela", "123", User.Role.USER, new ArrayList<>());
        when(accountDescribeService.getUser()).thenReturn(user);
        assertThat(underTest.describeAccount(), equalTo("Signed in with account 'bela'\nYou have not booked any tickets yet"));
    }

    @SneakyThrows
    @Test
    public void testExecuteShouldReturnAdminSignedInMassageAndNoBookingWhenTheUserIsLoggedInAndHasNoBookingsAndHasAdminAccess() {
        User user = new User("bela", "123", User.Role.ADMIN, new ArrayList<>());
        when(accountDescribeService.getUser()).thenReturn(user);
        assertThat(underTest.describeAccount(), equalTo("Signed in with privileged account 'bela'\nYou have not booked any tickets yet"));
    }

    @SneakyThrows
    @Test
    public void testExecuteShouldReturnAdminSignedInMassageAndProperlyFormattedBookingMassageWhenTheUserIsLoggedInAndHasBookingsAndHasAdminAccess() {
        Room room = new Room("Pedersoli", new ArrayList<>(), new ArrayList<>());
        Movie movie = new Movie("Spirited Away", "anime", 88, new ArrayList<>());
        Screening screening = new Screening(1, movie, room, LocalDateTime.parse("2021-04-24 00:44", dateTimeFormatter));
        Seat seat = new Seat(1, 1, 1, room, new ArrayList<>());
        Seat seat1 = new Seat(1, 2, 1, room, new ArrayList<>());
        Ticket ticket = new Ticket(1,1500, seat, new User(), screening);
        Ticket ticket1 = new Ticket(2, 1500, seat1, new User(), screening);
        User user = new User("bela", "123", User.Role.ADMIN, List.of(ticket,ticket1));

        when(accountDescribeService.getUser()).thenReturn(user);

        assertThat(underTest.describeAccount(), equalTo("Signed in with privileged account 'bela'\nYour previous bookings are\nSeats (1,1), (2,1) on Spirited Away in room Pedersoli starting at 2021-04-24 00:44 for 3000 HUF"));
    }

    @SneakyThrows
    @Test
    public void testExecuteShouldReturnUserSignedInMassageAndProperlyFormattedBookingMassageWhenTheUserIsLoggedInAndHasBookingsAndDoesNotHaveAdminAccess() {
        Room room = new Room("Pedersoli", new ArrayList<>(), new ArrayList<>());
        Movie movie = new Movie("Spirited Away", "anime", 88, new ArrayList<>());
        Screening screening = new Screening(1, movie, room, LocalDateTime.parse("2021-04-24 00:44", dateTimeFormatter));
        Seat seat = new Seat(1, 1, 1, room, new ArrayList<>());
        Seat seat1 = new Seat(1, 2, 1, room, new ArrayList<>());
        Ticket ticket = new Ticket(1,1500, seat, new User(), screening);
        Ticket ticket1 = new Ticket(2, 1500, seat1, new User(), screening);
        User user = new User("bela", "123", User.Role.USER, List.of(ticket,ticket1));

        when(accountDescribeService.getUser()).thenReturn(user);

        assertThat(underTest.describeAccount(), equalTo("Signed in with account 'bela'\nYour previous bookings are\nSeats (1,1), (2,1) on Spirited Away in room Pedersoli starting at 2021-04-24 00:44 for 3000 HUF"));

    }

    @SneakyThrows
    @Test
    public void testExecuteShouldReturnUserNotSignedInMassageWhenTheUserIsNotSignedIn() {
        when(accountDescribeService.getUser()).thenThrow(new UserNotLoggedInException());

        assertThat(underTest.describeAccount(), equalTo("You are not signed in"));
    }
}
