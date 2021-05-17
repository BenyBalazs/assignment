package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.User;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.user.AuthorizationService;
import com.epam.training.ticketservice.utils.ActionResult;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ScreeningServiceTest {

    private static final Room roomOfScreening  = new Room("Pedersoli", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    private static final Movie movie = new Movie("Spirited Away", "anime", 88, new ArrayList<>(), new ArrayList<>());
    private static final User basicUser = new User("bela", "123", User.Role.USER, new ArrayList<>());
    private static final User adminUser = new User("bela", "123", User.Role.ADMIN, new ArrayList<>());

    ScreeningService screeningService;
    @MockBean
    ScreeningRepository screeningRepository;
    @MockBean
    MovieRepository movieRepository;
    @MockBean
    RoomRepository roomRepository;
    AuthorizationService authorizationService;
    @MockBean
    ActiveUserStore activeUserStore;
    @MockBean
    ScreeningValidator screeningValidator;

    DateTimeFormatter dateTimeFormatter;

    @BeforeEach
    public void setUp() {
        activeUserStore =  Mockito.mock(ActiveUserStore.class);
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        authorizationService = new AuthorizationService(activeUserStore);
        screeningRepository = Mockito.mock(ScreeningRepository.class);
        movieRepository = Mockito.mock(MovieRepository.class);
        roomRepository = Mockito.mock(RoomRepository.class);
        screeningValidator = Mockito.mock(ScreeningValidator.class);
        screeningService = new ScreeningService(screeningRepository, movieRepository, roomRepository, screeningValidator, authorizationService);
    }

    @Test
    public void testCreateScreeningShouldThrowUserNotLoggedInExceptionWhenNoUserIsLoggedIn() {
        when(activeUserStore.getActiveUser()).thenReturn(null);
        assertThrows(UserNotLoggedInException.class, () -> screeningService.createScreening("asd", "asd", LocalDateTime.now()));
    }

    @Test
    public void testCreateScreeningShouldThrowNotAuthorizedOperationExceptionWhenTheUserDoesNotHaveTheGivenRoles() {
        when(activeUserStore.getActiveUser()).thenReturn(basicUser);
        assertThrows(NotAuthorizedOperationException.class, () -> screeningService.createScreening("asd", "asd", LocalDateTime.now()));
    }

    @SneakyThrows
    @Test
    public void testCreateScreeningShouldReturnProperActionResultWhenTheGivenMovieIsNotFound() {
        when(activeUserStore.getActiveUser()).thenReturn(adminUser);
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.empty());
        assertThat(screeningService.createScreening("asd", "asd", LocalDateTime.now()), equalTo(new ActionResult("Movie does not exist", false)));
    }

    @SneakyThrows
    @Test
    public void testCreateScreeningShouldReturnProperActionResultWhenTheGivenRoomIsNotFound() {
        when(activeUserStore.getActiveUser()).thenReturn(adminUser);
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.empty());
        assertThat(screeningService.createScreening("asd", "asd", LocalDateTime.now()), equalTo(new ActionResult("Room does not exist", false)));
    }

}
