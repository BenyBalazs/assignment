package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.data.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class ScreeningServiceTest {

    ScreeningService screeningService;
    @MockBean
    ScreeningRepository screeningRepository;
    @MockBean
    MovieRepository movieRepository;
    @MockBean
    RoomRepository roomRepository;
    List<Screening> screeningList;
    List<Movie> movieList;
    List<Room> roomList;
    DateTimeFormatter dateTimeFormatter;

    @BeforeEach
    public void setUp() {
        screeningList = new ArrayList<>();
        movieList = new ArrayList<>();
        roomList = new ArrayList<>();
        screeningService = new ScreeningService(screeningRepository, movieRepository, roomRepository, new ScreeningValidator());
        movieRepository = Mockito.mock(MovieRepository.class);
        when(this.movieRepository.findById(Mockito.any(String.class)))
                .then( x -> movieList.stream().filter(y -> x.getArgument(0).equals(y.getTitle())).findFirst());

        doAnswer(x -> movieList.add(x.getArgument(0)))
                .when(movieRepository).save(Mockito.mock(Movie.class));

        doAnswer(x -> movieList.remove(x.getArgument(0)))
                .when(movieRepository).delete(Mockito.any(Movie.class));

        when(this.movieRepository.findAll()).then(x -> movieList);

        roomRepository = Mockito.mock(RoomRepository.class);
        when(this.roomRepository.findById(Mockito.any(String.class)))
                .then( x -> roomList.stream().filter(y -> x.getArgument(0).equals(y.getRoomName())).findFirst());

        doAnswer(x -> roomList.add(x.getArgument(0)))
                .when(roomRepository).save(Mockito.mock(Room.class));

        doAnswer(x -> roomList.remove(x.getArgument(0)))
                .when(roomRepository).delete(Mockito.any(Room.class));

        when(this.roomRepository.findAll()).thenReturn(roomList);

        screeningRepository = Mockito.mock(ScreeningRepository.class);
        when(this.screeningRepository.findById(Mockito.any(Integer.class)))
                .then( x -> roomList.stream().filter(y -> x.getArgument(0).equals(y.getRoomName())).findFirst());

        doAnswer(x -> roomList.add(x.getArgument(0)))
                .when(screeningRepository).save(Mockito.mock(Screening.class));

        doAnswer(x -> roomList.remove(x.getArgument(0)))
                .when(screeningRepository).delete(Mockito.any(Screening.class));

        when(this.screeningRepository.findAll()).thenReturn(screeningList);

        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Screening screening = new Screening();
        screening.setId(1);
        screening.setStartOfScreening(LocalDateTime.parse("2021-04-20 16:12",dateTimeFormatter));
        Movie movie = new Movie();
        movie.setTitle("asd");
        movie.setLength(120);
        movie.setGenre("asdasd");
        screening.setMovie(movie);
        Room room = new Room();
        room.setRoomName("asdasd");
        screening.setRoomOfScreening(room);
        movieList.add(movie);
        roomList.add(room);
        screeningList.add(screening);
    }
}
