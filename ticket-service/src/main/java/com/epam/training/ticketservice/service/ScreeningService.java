package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.exception.OverlappingScreeningException;
import com.epam.training.ticketservice.exception.ScreeningInTheBrakePeriodException;
import com.epam.training.ticketservice.service.interfaces.ScreeningServiceInterface;
import com.epam.training.ticketservice.service.interfaces.ScreeningHelperUtilityInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScreeningService implements ScreeningServiceInterface {

    ScreeningRepository screeningRepository;
    MovieRepository movieRepository;
    RoomRepository roomRepository;
    ScreeningHelperUtilityInterface screeningValidator;
    AuthorizationService authorizationService;

    public ScreeningService(ScreeningRepository screeningRepository,
                            MovieRepository movieRepository,
                            RoomRepository roomRepository,
                            ScreeningHelperUtilityInterface screeningValidator,
                            AuthorizationService authorizationService) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.screeningValidator = screeningValidator;
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean createScreening(String movieTitle, String roomName, LocalDateTime startOfScreening)
            throws OverlappingScreeningException, ScreeningInTheBrakePeriodException {

        Movie movieToScreen = movieRepository.findById(movieTitle).orElse(null);

        if (movieToScreen == null) {
            return false;
        }

        Room roomOfScreening = roomRepository.findById(roomName).orElse(null);

        if (roomOfScreening == null) {
            return false;
        }

        List<Screening> screeningsInTheSameRoom = screeningRepository.findAllByRoomOfScreening(roomOfScreening);

        if (screeningValidator.isOverlappingScreening(screeningsInTheSameRoom, startOfScreening)) {
            throw new OverlappingScreeningException();
        }

        if (screeningValidator.screeningInTheBrakePeriod(screeningsInTheSameRoom, startOfScreening)) {
            throw new ScreeningInTheBrakePeriodException();
        }

        screeningRepository.save(createScreeningInstance(movieToScreen, roomOfScreening, startOfScreening));

        return true;
    }

    @Override
    public boolean deleteScreening(String movieTitle, String roomName, LocalDateTime startOfScreening) {

        Optional<Movie> movie = movieRepository.findById(movieTitle);
        Optional<Room> room = roomRepository.findById(roomName);

        if (movie.isEmpty() || room.isEmpty()) {
            return false;
        }

        Screening screeningToDelete = screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(movie.get(), room.get(), startOfScreening);

        screeningRepository.delete(screeningToDelete);

        return false;
    }

    @Override
    public List<Screening> getAllScreening() {
        return screeningRepository.findAll();
    }

    private Screening createScreeningInstance(Movie movieToScreen, Room roomOfScreening,
                                              LocalDateTime startOfScreening) {
        Screening screening = new Screening();
        screening.setRoomOfScreening(roomOfScreening);
        screening.setMovie(movieToScreen);
        screening.setStartOfScreening(startOfScreening);
        screening.setEndOfScreening(screeningValidator.addMinutesToDateTime(startOfScreening,
                movieToScreen.getLength()));

        return screening;
    }
}
