package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.dao.Seat;
import com.epam.training.ticketservice.data.dao.Ticket;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.data.repository.SeatRepository;
import com.epam.training.ticketservice.data.repository.TicketRepository;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.presentation.cli.configuration.CliConfiguration;
import com.epam.training.ticketservice.service.user.AuthorizationService;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.utils.BookingActionResult;
import com.epam.training.ticketservice.utils.SeatIntPair;
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
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BookingService.class, AuthorizationService.class, ActiveUserStore.class, CliConfiguration.class, BookingServiceHelper.class})
public class BookingServiceTest {

    BookingService underTest;
    BookingServiceHelper bookingServiceHelper;
    @MockBean
    SeatRepository seatRepository;
    @Autowired
    ActiveUserStore activeUserStore;
    @Autowired
    AuthorizationService authorizationService;
    @MockBean
    ScreeningRepository screeningRepository;
    @MockBean
    TicketRepository ticketRepository;
    @MockBean
    RoomRepository roomRepository;
    @MockBean
    MovieRepository movieRepository;
    @Autowired
    DateTimeFormatter dateTimeFormatter;

    @BeforeEach
    public void setUp() {
        screeningRepository = Mockito.mock(ScreeningRepository.class);
        seatRepository = Mockito.mock(SeatRepository.class);
        ticketRepository = Mockito.mock(TicketRepository.class);
        roomRepository = Mockito.mock(RoomRepository.class);
        movieRepository = Mockito.mock(MovieRepository.class);
        bookingServiceHelper = new BookingServiceHelper(activeUserStore, seatRepository, ticketRepository);
        underTest = new BookingService(authorizationService,screeningRepository, ticketRepository, roomRepository, movieRepository ,bookingServiceHelper);
    }

    @Test
    public void testBookSeatShouldThrowUserNotLoggedInExceptionWhenTheUserNotLoggedIn() {
        activeUserStore.setActiveUser(null);
        assertThrows(UserNotLoggedInException.class, () -> underTest.bookSeat("asd", "asd", LocalDateTime.now(), new ArrayList<>()));
    }

    @SneakyThrows
    @Test
    public void testBookSeatShouldReturnTheProperBookingActionResultWhenMovieIsNotFound() {

        activeUserStore.setActiveUser(new User("asd", "asd", User.Role.ADMIN));
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.empty());

        assertThat(underTest.bookSeat("asd", "ads", LocalDateTime.now(), new ArrayList<>()), equalTo(new BookingActionResult("NoMovie", false)));

    }

    @SneakyThrows
    @Test
    public void testBookSeatShouldReturnTheProperBookingActionResultWhenMovieIsFoundButNoRoomIsFound() {

        activeUserStore.setActiveUser(new User("asd", "asd", User.Role.ADMIN));
        Movie movie = new Movie("asd", "asd", 88, new ArrayList<>());
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.empty());

        assertThat(underTest.bookSeat("asd", "ads", LocalDateTime.now(), new ArrayList<>()), equalTo(new BookingActionResult("NoRoom", false)));

    }
    @SneakyThrows
    @Test
    public void testBookSeatShouldReturnTheProperBookingActionResultWhenMovieIsFoundRoomIsFoundButNoScreeningIsFound() {

        activeUserStore.setActiveUser(new User("asd", "asd", User.Role.ADMIN));
        Movie movie = new Movie("asd", "asd", 88, new ArrayList<>());
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        Room room = new Room("asd", new ArrayList<>(),1, 2, new ArrayList<>());
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(room));
        when(screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(Mockito.any(Movie.class), Mockito.any(Room.class), Mockito.any(LocalDateTime.class))).thenReturn(null);

        assertThat(underTest.bookSeat("asd", "ads", LocalDateTime.now(), new ArrayList<>()), equalTo(new BookingActionResult("NoScreening", false)));

    }

    @SneakyThrows
    @Test
    public void testBookSeatShouldReturnTheProperBookingActionResultWhenEverythingIsFoundButSeatsAreNotInTheRoom() {
        activeUserStore.setActiveUser(new User("asd", "asd", User.Role.ADMIN));
        Movie movie = new Movie("asd", "asd", 88, new ArrayList<>());
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        Room room = new Room("asd", new ArrayList<>(),1, 2, new ArrayList<>());
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(room));
        Screening screening = new Screening(1, movie, room, LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter), LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter).plusMinutes(movie.getLength()));
        when(screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(Mockito.any(Movie.class), Mockito.any(Room.class), Mockito.any(LocalDateTime.class))).thenReturn(screening);
        when(seatRepository.findByRoomAndRowPositionAndColPosition(Mockito.any(Room.class), Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(null);

        assertThat(underTest.bookSeat("asd", "asd", LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter), List.of(new SeatIntPair(1,2))), equalTo(new BookingActionResult("NoSeat", false, new SeatIntPair(1,2))));
    }

    @SneakyThrows
    @Test
    public void testBookSeatShouldReturnTheProperBookingActionResultWhenEverythingIsFoundButSeatIsAlreadyTaken() {
        activeUserStore.setActiveUser(new User("asd", "asd", User.Role.ADMIN));
        Movie movie = new Movie("asd", "asd", 88, new ArrayList<>());
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        Room room = new Room("asd", new ArrayList<>(),1, 2, new ArrayList<>());
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(room));
        Screening screening = new Screening(1, movie, room, LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter), LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter).plusMinutes(movie.getLength()));
        when(screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(Mockito.any(Movie.class), Mockito.any(Room.class), Mockito.any(LocalDateTime.class))).thenReturn(screening);
        Seat seat = new Seat(1, 1, 6, room, new ArrayList<>());
        when(seatRepository.findByRoomAndRowPositionAndColPosition(Mockito.any(Room.class), Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(seat);
        Ticket ticket = new Ticket(1, 1500, seat, activeUserStore.getActiveUser(), screening);
        when(ticketRepository.findBySeatAndScreening(Mockito.any(Seat.class), Mockito.any(Screening.class))).thenReturn(ticket);

        assertThat(underTest.bookSeat("asd", "asd", LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter), List.of(new SeatIntPair(1,6))), equalTo(new BookingActionResult("Taken", false, new SeatIntPair(1, 6))));

    }

    @SneakyThrows
    @Test
    public void testBookSeatShouldReturnTheProperBookingActionResultWhenEverythingIsFine() {
        activeUserStore.setActiveUser(new User("asd", "asd", User.Role.ADMIN));
        Movie movie = new Movie("asd", "asd", 88, new ArrayList<>());
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        Room room = new Room("asd", new ArrayList<>(),1, 2, new ArrayList<>());
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(room));
        Screening screening = new Screening(1, movie, room, LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter), LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter).plusMinutes(movie.getLength()));
        when(screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(Mockito.any(Movie.class), Mockito.any(Room.class), Mockito.any(LocalDateTime.class))).thenReturn(screening);
        Seat seat = new Seat(1, 1, 6, room, new ArrayList<>());
        when(seatRepository.findByRoomAndRowPositionAndColPosition(Mockito.any(Room.class), Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(seat);
        when(ticketRepository.findBySeatAndScreening(Mockito.any(Seat.class), Mockito.any(Screening.class))).thenReturn(null);

        assertThat(underTest.bookSeat("asd", "asd", LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter), List.of(new SeatIntPair(1,6))), equalTo(new BookingActionResult("SeatsBooked", true, 1500)));
    }




}
