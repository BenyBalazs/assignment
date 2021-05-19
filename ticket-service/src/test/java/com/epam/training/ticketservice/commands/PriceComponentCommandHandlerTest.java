package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.presentation.cli.configuration.CliConfiguration;
import com.epam.training.ticketservice.presentation.cli.handler.PriceComponentCommandHandler;
import com.epam.training.ticketservice.service.PriceComponentService;
import com.epam.training.ticketservice.utils.ActionResult;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CliConfiguration.class})
public class PriceComponentCommandHandlerTest {

    private static final ActionResult SUCCESS_RESULT = new ActionResult("ComponentAttached", true);
    private static final ActionResult NO_PRICE_COMPONENT = new ActionResult("NoPriceComponent", false);
    private static final ActionResult NO_MOVE_RESULT = new ActionResult("NoMovie", false);
    private static final ActionResult NO_ROOM_RESULT  = new ActionResult("NoRoom", false);
    private static final ActionResult NO_SCREENING_RESULT = new ActionResult("NoScreening", false);
    private static final String MOVIE_TITLE = "asd";
    private static final String ROOM_NAME = "asd";
    private static final String START_OF_SCREENING_STRING = "2021-03-15 10:45";
    private static final LocalDateTime START_OF_SCREENING_DATE = LocalDateTime.now();
    private static final String COMPONENT_NAME = "asd";
    private static final int PRICE = 406;
    
    private PriceComponentCommandHandler underTest;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @MockBean
    private PriceComponentService priceComponentService;
    
    @BeforeEach
    public void setUp() {
        priceComponentService = mock(PriceComponentService.class);
        underTest = new PriceComponentCommandHandler(priceComponentService, dateTimeFormatter);
    }
    
    @SneakyThrows
    @Test
    public void testAttachComponentToScreeningShouldReturnNoScreeningMessageWhenNoScreeningWasFound() {
        when(priceComponentService.attachPriceComponentToScreening(any(), any(), any(), any())).thenReturn(NO_SCREENING_RESULT);

        String actual = underTest.attachComponentToScreening(COMPONENT_NAME, MOVIE_TITLE, ROOM_NAME, START_OF_SCREENING_STRING);

        assertThat(actual, equalTo("No Screening was fount with these variables"));

    }

    @SneakyThrows
    @Test
    public void testAttachComponentToScreeningShouldReturnSuccessMessage() {
        when(priceComponentService.attachPriceComponentToScreening(any(), any(), any(), any())).thenReturn(SUCCESS_RESULT);

        String actual = underTest.attachComponentToScreening(COMPONENT_NAME, MOVIE_TITLE, ROOM_NAME, START_OF_SCREENING_STRING);

        assertThat(actual, equalTo("Price Component is attached to the screening"));
    }

    @SneakyThrows
    @Test
    public void testAttachComponentToScreeningShouldReturnLogInErrorMessageWhenNoOneIsLoggedIn() {
        when(priceComponentService.attachPriceComponentToScreening(any(), any(), any(), any())).thenThrow(new UserNotLoggedInException());

        String actual = underTest.attachComponentToScreening(COMPONENT_NAME, MOVIE_TITLE, ROOM_NAME, START_OF_SCREENING_STRING);

        assertThat(actual, equalTo("You are not signed in"));
    }

    @SneakyThrows
    @Test
    public void testAttachComponentToScreeningShouldReturnAuthenticationErrorWhenServiceThrowsNotAuthorizedOperationException() {
        when(priceComponentService.attachPriceComponentToScreening(any(), any(), any(), any())).thenThrow(new NotAuthorizedOperationException());

        String actual = underTest.attachComponentToScreening(COMPONENT_NAME, MOVIE_TITLE, ROOM_NAME, START_OF_SCREENING_STRING);

        assertThat(actual, equalTo("This is and admin command"));
    }

    @SneakyThrows
    @Test
    public void testAttachComponentToRoomShouldReturnNoScreeningMessageWhenNoRoomWasFound() {
        when(priceComponentService.attachPriceComponentToRoom(any(), any())).thenReturn(NO_ROOM_RESULT);

        String actual = underTest.attachComponentToRoom(COMPONENT_NAME, ROOM_NAME);

        assertThat(actual, equalTo("Whe have no room with this name"));

    }

    @SneakyThrows
    @Test
    public void testAttachComponentToRoomShouldReturnSuccessMessageWhenEverythingWentFine() {
        when(priceComponentService.attachPriceComponentToRoom(any(), any())).thenReturn(SUCCESS_RESULT);

        String actual = underTest.attachComponentToRoom(COMPONENT_NAME, ROOM_NAME);

        assertThat(actual, equalTo("Price Component is attached to the room"));
    }

    @SneakyThrows
    @Test
    public void testAttachComponentToRoomShouldReturnLogInErrorMessageWhenNoOneIsLoggedIn() {
        when(priceComponentService.attachPriceComponentToRoom(any(), any())).thenThrow(new UserNotLoggedInException());

        String actual = underTest.attachComponentToRoom(COMPONENT_NAME, ROOM_NAME);

        assertThat(actual, equalTo("You are not signed in"));
    }

    @SneakyThrows
    @Test
    public void testAttachComponentToRoomShouldReturnAuthenticationErrorWhenServiceThrowsNotAuthorizedOperationException() {
        when(priceComponentService.attachPriceComponentToRoom(any(), any())).thenThrow(new NotAuthorizedOperationException());

        String actual = underTest.attachComponentToRoom(COMPONENT_NAME, ROOM_NAME);

        assertThat(actual, equalTo("This is and admin command"));
    }

    //------------

    @SneakyThrows
    @Test
    public void testAttachComponentToMovieShouldReturnNoScreeningMessageWhenMovieWasFound() {
        when(priceComponentService.attachPriceComponentToMovie(any(), any())).thenReturn(NO_MOVE_RESULT);

        String actual = underTest.attachComponentToMovie(COMPONENT_NAME, MOVIE_TITLE);

        assertThat(actual, equalTo("Whe have no movies with this title"));

    }

    @SneakyThrows
    @Test
    public void testAttachComponentToMovieShouldReturnSuccessMessageWhenEverythingWentFine() {
        when(priceComponentService.attachPriceComponentToMovie(any(), any())).thenReturn(SUCCESS_RESULT);

        String actual = underTest.attachComponentToMovie(COMPONENT_NAME, MOVIE_TITLE);

        assertThat(actual, equalTo("Price Component is attached to the movie"));
    }

    @SneakyThrows
    @Test
    public void testAttachComponentToMovieShouldReturnLogInErrorMessageWhenNoOneIsLoggedIn() {
        when(priceComponentService.attachPriceComponentToMovie(any(), any())).thenThrow(new UserNotLoggedInException());

        String actual = underTest.attachComponentToMovie(COMPONENT_NAME, MOVIE_TITLE);

        assertThat(actual, equalTo("You are not signed in"));
    }

    @SneakyThrows
    @Test
    public void testAttachComponentToMovieShouldReturnAuthenticationErrorWhenServiceThrowsNotAuthorizedOperationException() {
        when(priceComponentService.attachPriceComponentToMovie(any(), any())).thenThrow(new NotAuthorizedOperationException());

        String actual = underTest.attachComponentToMovie(COMPONENT_NAME, MOVIE_TITLE);

        assertThat(actual, equalTo("This is and admin command"));
    }

    @SneakyThrows
    @Test
    public void testCreatePriceComponentShouldReturnAuthenticationErrorWhenServiceThrowsNotAuthorizedOperationException() {
        when(priceComponentService.createPriceComponent(any(), anyInt())).thenThrow(new NotAuthorizedOperationException());

        String actual = underTest.createPriceComponent(COMPONENT_NAME, PRICE);

        assertThat(actual, equalTo("This is and admin command"));
    }

    @SneakyThrows
    @Test
    public void testCreatePriceComponentShouldReturnAuthenticationErrorWhenNoOneIsLoggedIn() {
        when(priceComponentService.createPriceComponent(any(), anyInt())).thenThrow(new UserNotLoggedInException());

        String actual = underTest.createPriceComponent(COMPONENT_NAME, PRICE);

        assertThat(actual, equalTo("You are not signed in"));
    }

    @SneakyThrows
    @Test
    public void testCreatePriceComponentShouldReturnCreatedMessageWhenServiceReturnsTrue() {
        when(priceComponentService.createPriceComponent(any(), anyInt())).thenReturn(true);

        String actual = underTest.createPriceComponent(COMPONENT_NAME, PRICE);

        assertThat(actual, equalTo("Price Component created"));
    }

    @SneakyThrows
    @Test
    public void testCreatePriceComponentShouldReturnAlreadyExistsMessageWhenServiceReturnsFalse() {
        when(priceComponentService.createPriceComponent(any(), anyInt())).thenReturn(false);

        String actual = underTest.createPriceComponent(COMPONENT_NAME, PRICE);

        assertThat(actual, equalTo("Price Component already exists!"));
    }
}
