package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.data.entity.Seat;
import com.epam.training.ticketservice.data.entity.Ticket;
import com.epam.training.ticketservice.data.entity.User;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.data.repository.SeatRepository;
import com.epam.training.ticketservice.data.repository.TicketRepository;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.presentation.cli.configuration.CliConfiguration;
import com.epam.training.ticketservice.service.user.AuthorizationService;
import com.epam.training.ticketservice.service.utils.BookingServiceHelper;
import com.epam.training.ticketservice.service.utils.PriceCalculator;
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

    private static final Room roomOfScreening  = new Room("Pedersoli", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    private static final Movie movie = new Movie("Spirited Away", "anime", 88, new ArrayList<>(), new ArrayList<>());
    private static final User basicUser = new User("bela", "123", User.Role.USER, new ArrayList<>());
    private static final User adminUser = new User("bela", "123", User.Role.ADMIN, new ArrayList<>());

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
    @MockBean
    PriceCalculator priceCalculator;
    @Autowired
    DateTimeFormatter dateTimeFormatter;
    Screening targetScreening;

    @BeforeEach
    public void setUp() {
        screeningRepository = Mockito.mock(ScreeningRepository.class);
        seatRepository = Mockito.mock(SeatRepository.class);
        ticketRepository = Mockito.mock(TicketRepository.class);
        roomRepository = Mockito.mock(RoomRepository.class);
        movieRepository = Mockito.mock(MovieRepository.class);
        priceCalculator = Mockito.mock(PriceCalculator.class);
        bookingServiceHelper = new BookingServiceHelper(activeUserStore, seatRepository, ticketRepository, priceCalculator);
        underTest = new BookingService(authorizationService,screeningRepository, ticketRepository, roomRepository, movieRepository ,bookingServiceHelper);
        targetScreening = new Screening(1, movie, roomOfScreening, LocalDateTime.parse("2021-04-24 00:44", dateTimeFormatter), new ArrayList<>());
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

        activeUserStore.setActiveUser(adminUser);
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.empty());

        assertThat(underTest.bookSeat("asd", "ads", LocalDateTime.now(), new ArrayList<>()), equalTo(new BookingActionResult("NoRoom", false)));

    }
    @SneakyThrows
    @Test
    public void testBookSeatShouldReturnTheProperBookingActionResultWhenMovieIsFoundRoomIsFoundButNoScreeningIsFound() {

        activeUserStore.setActiveUser(new User("asd", "asd", User.Role.ADMIN));
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(roomOfScreening));
        when(screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(Mockito.any(Movie.class), Mockito.any(Room.class), Mockito.any(LocalDateTime.class))).thenReturn(null);

        assertThat(underTest.bookSeat("asd", "ads", LocalDateTime.now(), new ArrayList<>()), equalTo(new BookingActionResult("NoScreening", false)));

    }

    @SneakyThrows
    @Test
    public void testBookSeatShouldReturnTheProperBookingActionResultWhenEverythingIsFoundButSeatsAreNotInTheRoom() {
        activeUserStore.setActiveUser(new User("asd", "asd", User.Role.ADMIN));
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(roomOfScreening));
        Screening screening = targetScreening;
        when(screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(Mockito.any(Movie.class), Mockito.any(Room.class), Mockito.any(LocalDateTime.class))).thenReturn(screening);
        when(seatRepository.findByRoomAndRowPositionAndColPosition(Mockito.any(Room.class), Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(null);

        assertThat(underTest.bookSeat("asd", "asd", LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter), List.of(new SeatIntPair(1,2))), equalTo(new BookingActionResult("NoSeat", false, new SeatIntPair(1,2))));
    }

    @SneakyThrows
    @Test
    public void testBookSeatShouldReturnTheProperBookingActionResultWhenEverythingIsFoundButSeatIsAlreadyTaken() {
        activeUserStore.setActiveUser(new User("asd", "asd", User.Role.ADMIN));
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(roomOfScreening));
        Screening screening = targetScreening;
        when(screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(Mockito.any(Movie.class), Mockito.any(Room.class), Mockito.any(LocalDateTime.class))).thenReturn(screening);
        Seat seat = new Seat(1, 1, 6, roomOfScreening, new ArrayList<>());
        when(seatRepository.findByRoomAndRowPositionAndColPosition(Mockito.any(Room.class), Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(seat);
        Ticket ticket = new Ticket(1, 1500, seat, activeUserStore.getActiveUser(), screening);
        when(ticketRepository.findBySeatAndScreening(Mockito.any(Seat.class), Mockito.any(Screening.class))).thenReturn(ticket);

        assertThat(underTest.bookSeat("asd", "asd", LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter), List.of(new SeatIntPair(1,6))), equalTo(new BookingActionResult("Taken", false, new SeatIntPair(1, 6))));

    }

    @SneakyThrows
    @Test
    public void testBookSeatShouldReturnTheProperBookingActionResultWhenEverythingIsFine() {
        activeUserStore.setActiveUser(new User("asd", "asd", User.Role.ADMIN));
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(roomOfScreening));
        Screening screening = targetScreening;
        when(screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(Mockito.any(Movie.class), Mockito.any(Room.class), Mockito.any(LocalDateTime.class))).thenReturn(screening);
        Seat seat = new Seat(1, 1, 6, roomOfScreening, new ArrayList<>());
        when(seatRepository.findByRoomAndRowPositionAndColPosition(Mockito.any(Room.class), Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(seat);
        when(ticketRepository.findBySeatAndScreening(Mockito.any(Seat.class), Mockito.any(Screening.class))).thenReturn(null);
        when(priceCalculator.calculateTicketPrice(Mockito.any(Screening.class), Mockito.any(Seat.class))).thenReturn(1500);

        assertThat(underTest.bookSeat("asd", "asd", LocalDateTime.parse("2021-04-24 19:00", dateTimeFormatter), List.of(new SeatIntPair(1,6))), equalTo(new BookingActionResult("SeatsBooked", true, 1500)));
    }




}
