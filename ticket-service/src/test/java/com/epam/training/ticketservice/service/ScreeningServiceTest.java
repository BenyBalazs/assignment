package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.service.user.AuthorizationService;
import com.epam.training.ticketservice.utils.ActionResult;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ScreeningServiceTest {

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
    List<Screening> screeningList;
    List<Movie> movieList;
    List<Room> roomList;
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
        when(activeUserStore.getActiveUser()).thenReturn(new User("bela","123", User.Role.USER));
        assertThrows(NotAuthorizedOperationException.class, () -> screeningService.createScreening("asd", "asd", LocalDateTime.now()));
    }

    @SneakyThrows
    @Test
    public void testCreateScreeningShouldReturnProperActionResultWhenTheGivenMovieIsNotFound() {
        when(activeUserStore.getActiveUser()).thenReturn(new User("bela","123", User.Role.ADMIN));
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.empty());
        assertThat(screeningService.createScreening("asd", "asd", LocalDateTime.now()), equalTo(new ActionResult("Movie does not exist", false)));
    }

    @SneakyThrows
    @Test
    public void testCreateScreeningShouldReturnProperActionResultWhenTheGivenRoomIsNotFound() {
        when(activeUserStore.getActiveUser()).thenReturn(new User("bela","123", User.Role.ADMIN));
        when(movieRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(new Movie("asd", "asdasd", 123, null)));
        when(roomRepository.findById(Mockito.any(String.class))).thenReturn(Optional.empty());
        assertThat(screeningService.createScreening("asd", "asd", LocalDateTime.now()), equalTo(new ActionResult("Room does not exist", false)));
    }

}
