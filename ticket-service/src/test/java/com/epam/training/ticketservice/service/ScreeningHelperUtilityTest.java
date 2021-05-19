package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.service.interfaces.ScreeningHelperUtilityInterface;
import com.epam.training.ticketservice.service.utils.ScreeningValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ScreeningHelperUtilityTest {

    ScreeningHelperUtilityInterface screeningHelperUtility;
    List<Screening> screeningList;
    DateTimeFormatter dateTimeFormatter;

    @BeforeEach
    public void setUp() {
        screeningHelperUtility = new ScreeningValidator();
        screeningList = new ArrayList<>();

        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Screening screening1 = new Screening();
        screening1.setId(1);
        screening1.setStartOfScreening(LocalDateTime.parse("2021-04-20 16:12", dateTimeFormatter));
        Movie movie = new Movie();
        movie.setTitle("asd");
        movie.setLength(120);
        movie.setGenre("asdasd");
        screening1.setMovie(movie);
        Room room = new Room();
        room.setRoomName("asdasd");
        screening1.setRoomOfScreening(room);
        screeningList.add(screening1);

        Screening screening2 = new Screening();
        screening2.setId(1);
        screening2.setStartOfScreening(LocalDateTime.parse("2021-04-20 17:22", dateTimeFormatter));
        screening2.setMovie(movie);
        screening2.setRoomOfScreening(room);
        screeningList.add(screening2);

    }

    @Test
    public void testisOverlappingScreeningShouldReturnTrueWhenTheScreeningTimeOverlapsWithExistingScreenings() {
        LocalDateTime timeOfScreening = LocalDateTime.parse("2021-04-20 16:53", dateTimeFormatter);

        assertThat(screeningHelperUtility.isOverlappingScreening(screeningList, timeOfScreening), equalTo(true));
    }

    @Test
    public void testisOverlappingScreeningShouldReturnTrueWhenTheScreeningTimeDoesNotOverlapsWithExistingScreenings() {
        LocalDateTime timeOfScreening = LocalDateTime.parse("2021-04-21 17:53", dateTimeFormatter);

        assertThat(screeningHelperUtility.isOverlappingScreening(screeningList, timeOfScreening), equalTo(false));
    }

    @Test
    public void testScreeningInTheBrakePeriodShouldReturnTrueWhenTheScreeningStartIsInTheTenMinutesBrakingPeriod() {
        LocalDateTime timeOfScreening = LocalDateTime.parse("2021-04-20 18:21", dateTimeFormatter);

        assertThat(screeningHelperUtility.screeningInTheBrakePeriod(screeningList, timeOfScreening), equalTo(true));
    }

    @Test
    public void testScreeningInTheBrakePeriodShouldReturnTrueWhenTheScreeningDoesNotStartIsInTheTenMinutesBrakingPeriod() {
        LocalDateTime timeOfScreening = LocalDateTime.parse("2021-04-20 18:25", dateTimeFormatter);

        assertThat(screeningHelperUtility.screeningInTheBrakePeriod(screeningList, timeOfScreening), equalTo(false));
    }

    @Test
    public void testBetweenTwoDatesShouldReturnTrueWhenTheFistParamIsBetweenTheSecondAndTheThird() {
        LocalDateTime timeOfScreening = LocalDateTime.parse("2021-04-20 16:53", dateTimeFormatter);
        LocalDateTime startOfScreening = LocalDateTime.parse("2021-04-20 16:10", dateTimeFormatter);
        LocalDateTime endOfScreening = LocalDateTime.parse("2021-04-20 17:00", dateTimeFormatter);
        assertThat(screeningHelperUtility.betweenTwoDates(timeOfScreening, startOfScreening, endOfScreening), equalTo(true));
    }

    @Test
    public void testBetweenTwoDatesShouldReturnTrueWhenTheFistParamIsNotBetweenTheSecondAndTheThird() {
        LocalDateTime timeOfScreening = LocalDateTime.parse("2021-04-20 17:53", dateTimeFormatter);
        LocalDateTime startOfScreening = LocalDateTime.parse("2021-04-20 16:10", dateTimeFormatter);
        LocalDateTime endOfScreening = LocalDateTime.parse("2021-04-20 17:00", dateTimeFormatter);
        assertThat(screeningHelperUtility.betweenTwoDates(timeOfScreening, startOfScreening, endOfScreening), equalTo(false));
    }

    @Test
    public void testBetweenTwoDatesShouldReturnTrueWhenTheFistParamEqualsToTheThird() {
        LocalDateTime timeOfScreening = LocalDateTime.parse("2021-04-20 17:00", dateTimeFormatter);
        LocalDateTime startOfScreening = LocalDateTime.parse("2021-04-20 16:10", dateTimeFormatter);
        LocalDateTime endOfScreening = LocalDateTime.parse("2021-04-20 17:00", dateTimeFormatter);
        assertThat(screeningHelperUtility.betweenTwoDates(timeOfScreening, startOfScreening, endOfScreening), equalTo(true));
    }

    @Test
    public void testBetweenTwoDatesShouldReturnTrueWhenTheFistParamEqualsToTheSecond() {
        LocalDateTime timeOfScreening = LocalDateTime.parse("2021-04-20 16:10", dateTimeFormatter);
        LocalDateTime startOfScreening = LocalDateTime.parse("2021-04-20 16:10", dateTimeFormatter);
        LocalDateTime endOfScreening = LocalDateTime.parse("2021-04-20 17:00", dateTimeFormatter);
        assertThat(screeningHelperUtility.betweenTwoDates(timeOfScreening, startOfScreening, endOfScreening), equalTo(true));
    }

    @Test
    public void testAddMinutesToDateTimeShouldReturnANewLocalDateTimeLengthMinutesLargerThanTheBase() {
        LocalDateTime timeOfScreening = LocalDateTime.parse("2021-04-20 16:10", dateTimeFormatter);
        LocalDateTime expected = LocalDateTime.parse("2021-04-20 16:30", dateTimeFormatter);

        assertThat(screeningHelperUtility.addMinutesToDateTime(timeOfScreening, 20), equalTo(expected));
    }
}
