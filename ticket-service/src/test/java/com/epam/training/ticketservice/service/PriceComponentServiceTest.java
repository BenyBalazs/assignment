package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.data.entity.PriceComponent;
import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.PriceComponentRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.utils.ActionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PriceComponentServiceTest {

    private static final int PRICE = 690;
    private static final String TITLE  = "SomeTitle";
    private static final String ROOM_NAME = "SomeRoom";
    private static final String PRICE_COMPONENT_NAME  = "SomePriceComponent";
    private static final LocalDateTime START_OF_SCREENING = LocalDateTime.now();
    private static final Room ROOM_OF_SCREENING  = new Room(ROOM_NAME, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    private static final Movie MOVIE  = new Movie(TITLE, "someGenre", 99, new ArrayList<>(), new ArrayList<>());
    private static final Screening FOUND_SCREENING = new Screening(1, MOVIE, ROOM_OF_SCREENING, START_OF_SCREENING, new ArrayList<>());
    private static final PriceComponent FOUND_PRICE_COMPONENT  = new PriceComponent(PRICE_COMPONENT_NAME, PRICE, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    private static final ActionResult NO_PRICE_COMPONENT_RESULT = new ActionResult("NoPriceComponent", false);
    private static final ActionResult SUCCESS  = new ActionResult("ComponentAttached", true);
    private static final ActionResult NO_ROOM_FOUND = new ActionResult("NoRoom", false);
    private static final ActionResult NO_SCREENING_FOUND  = new ActionResult("NoScreening", false);
    private static final ActionResult NO_MOVIE_FOUND = new ActionResult("NoMovie", false);

    PriceComponentService underTest;
    @MockBean
    MovieRepository movieRepository;
    @MockBean
    ScreeningRepository screeningRepository;
    @MockBean
    PriceComponentRepository priceComponentRepository;
    @MockBean
    RoomRepository roomRepository;

    @BeforeEach
    public void setUp() {
        movieRepository = mock(MovieRepository.class);
        screeningRepository = mock(ScreeningRepository.class);
        priceComponentRepository = mock(PriceComponentRepository.class);
        roomRepository = mock(RoomRepository.class);
        underTest = new PriceComponentService(movieRepository, screeningRepository, priceComponentRepository, roomRepository);
    }

    @Test
    public void testCreatePriceComponentShouldReturnFalseWhenComponentAlreadyExists() {
        when(priceComponentRepository.findById(any(String.class))).thenReturn(Optional.of(FOUND_PRICE_COMPONENT));

        boolean actual = underTest.createPriceComponent("someName", PRICE);

        assertThat(actual, equalTo(false));
    }

    @Test
    public void testCreatePriceComponentShouldReturnTrueWhenComponentIsCreated() {
        when(priceComponentRepository.findById(any(String.class))).thenReturn(Optional.empty());

        boolean actual = underTest.createPriceComponent("someName", PRICE);

        assertThat(actual, equalTo(true));
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldReturnTheProperActionResultWhenScreeningIsNotFound() {
        when(screeningRepository.findByMovieAndRoomOfScreeningAndStartOfScreening(any(Movie.class), any(Room.class), any(LocalDateTime.class))).thenReturn(null);

        ActionResult actual = underTest.attachPriceComponentToScreening(TITLE, ROOM_NAME, START_OF_SCREENING, PRICE_COMPONENT_NAME);

        assertThat(actual, equalTo(NO_SCREENING_FOUND));
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldReturnTheProperActionResultWhenPriceComponentIsNotFound() {
        when(screeningRepository.findByMovieAndRoomOfScreeningAndStartOfScreening(any(Movie.class), any(Room.class), any(LocalDateTime.class))).thenReturn(FOUND_SCREENING);
        when(movieRepository.findById(any())).thenReturn(Optional.of(MOVIE));
        when(roomRepository.findById(any())).thenReturn(Optional.of(ROOM_OF_SCREENING));
        when(priceComponentRepository.findById(any())).thenReturn(Optional.empty());

        ActionResult actual = underTest.attachPriceComponentToScreening(TITLE, ROOM_NAME, START_OF_SCREENING, PRICE_COMPONENT_NAME);

        assertThat(actual, equalTo(NO_PRICE_COMPONENT_RESULT));
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldReturnTheProperActionResultWhenEverythingWentOk() {
        when(priceComponentRepository.findById(any(String.class))).thenReturn(Optional.of(FOUND_PRICE_COMPONENT));
        when(screeningRepository.findByMovieAndRoomOfScreeningAndStartOfScreening(any(Movie.class), any(Room.class), any(LocalDateTime.class))).thenReturn(FOUND_SCREENING);
        when(movieRepository.findById(any())).thenReturn(Optional.of(MOVIE));
        when(roomRepository.findById(any())).thenReturn(Optional.of(ROOM_OF_SCREENING));

        ActionResult actual = underTest.attachPriceComponentToScreening(TITLE, ROOM_NAME, START_OF_SCREENING, PRICE_COMPONENT_NAME);

        assertThat(actual, equalTo(SUCCESS));
    }

    @Test
    public void testAttachPriceComponentToRoomShouldReturnTheProperActionResultWhenPriceComponentIsFound() {
        when(roomRepository.findById(any())).thenReturn(Optional.of(ROOM_OF_SCREENING));
        when(priceComponentRepository.findById(any(String.class))).thenReturn(Optional.empty());

        ActionResult actual = underTest.attachPriceComponentToRoom(ROOM_NAME, PRICE_COMPONENT_NAME);

        assertThat(actual, equalTo(NO_PRICE_COMPONENT_RESULT));
    }

    @Test
    public void testAttachPriceComponentToRoomShouldReturnTheProperActionResultWhenNoNoRoomIsFound() {
        when(roomRepository.findById(any())).thenReturn(Optional.empty());
        when(priceComponentRepository.findById(any(String.class))).thenReturn(Optional.of(FOUND_PRICE_COMPONENT));

        ActionResult actual = underTest.attachPriceComponentToRoom(ROOM_NAME, PRICE_COMPONENT_NAME );

        assertThat(actual, equalTo(NO_ROOM_FOUND));
    }

    @Test
    public void testAttachPriceComponentToRoomShouldReturnTheProperActionResultWhenNoPriceComponentIsFound() {
        when(roomRepository.findById(any())).thenReturn(Optional.of(ROOM_OF_SCREENING));
        when(priceComponentRepository.findById(any(String.class))).thenReturn(Optional.empty());

        ActionResult actual = underTest.attachPriceComponentToRoom(ROOM_NAME, PRICE_COMPONENT_NAME );

        assertThat(actual, equalTo(NO_PRICE_COMPONENT_RESULT));
    }

    @Test
    public void testAttachPriceComponentToRoomShouldReturnTheProperActionResultWhenEverythingWentFine() {
        when(roomRepository.findById(any())).thenReturn(Optional.of(ROOM_OF_SCREENING));
        when(priceComponentRepository.findById(any(String.class))).thenReturn(Optional.of(FOUND_PRICE_COMPONENT));

        ActionResult actual = underTest.attachPriceComponentToRoom(ROOM_NAME, PRICE_COMPONENT_NAME );

        assertThat(actual, equalTo(SUCCESS));
    }

    @Test
    public void testAttachPriceComponentToMovieShouldReturnTheProperActionResultWhenEverythingWentFine() {
        when(movieRepository.findById(any())).thenReturn(Optional.of(MOVIE));
        when(priceComponentRepository.findById(any(String.class))).thenReturn(Optional.of(FOUND_PRICE_COMPONENT));

        ActionResult actual = underTest.attachPriceComponentToMovie(TITLE, PRICE_COMPONENT_NAME );

        assertThat(actual, equalTo(SUCCESS));
    }

    @Test
    public void testAttachPriceComponentToMovieShouldReturnTheProperActionResultWhenNoPriceComponentIsFound() {
        when(movieRepository.findById(any())).thenReturn(Optional.of(MOVIE));
        when(priceComponentRepository.findById(any(String.class))).thenReturn(Optional.empty());

        ActionResult actual = underTest.attachPriceComponentToMovie(TITLE, PRICE_COMPONENT_NAME );

        assertThat(actual, equalTo(NO_PRICE_COMPONENT_RESULT));
    }

    @Test
    public void testAttachPriceComponentToMovieShouldReturnTheProperActionResultWhenNoMovieIsFound() {
        when(movieRepository.findById(any())).thenReturn(Optional.empty());
        when(priceComponentRepository.findById(any(String.class))).thenReturn(Optional.of(FOUND_PRICE_COMPONENT));

        ActionResult actual = underTest.attachPriceComponentToMovie(TITLE, PRICE_COMPONENT_NAME );

        assertThat(actual, equalTo(NO_MOVIE_FOUND));
    }
}
