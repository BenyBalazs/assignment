package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.utils.ActionResult;
import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.ScreeningHelperUtilityInterface;
import com.epam.training.ticketservice.service.interfaces.ScreeningServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScreeningService implements ScreeningServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger("ScreeningService.class");

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
    public ActionResult createScreening(String movieTitle, String roomName, LocalDateTime startOfScreening)
            throws UserNotLoggedInException, NotAuthorizedOperationException {

        authorizationService.userHasRoles(User.Role.ADMIN);

        Movie movieToScreen = movieRepository.findById(movieTitle).orElse(null);

        if (movieToScreen == null) {
            return new ActionResult("Movie does not exist", false);
        }

        Room roomOfScreening = roomRepository.findById(roomName).orElse(null);

        if (roomOfScreening == null) {
            return new ActionResult("Room does not exist", false);
        }

        List<Screening> screeningsInTheSameRoom = screeningRepository.findAllByRoomOfScreening(roomOfScreening);

        if (screeningValidator.isOverlappingScreening(screeningsInTheSameRoom, startOfScreening)) {
            return new ActionResult("Overlapping", false);
        }

        if (screeningValidator.screeningInTheBrakePeriod(screeningsInTheSameRoom, startOfScreening)) {
            return new ActionResult("BrakePeriod", false);
        }

        logger.trace("Inserting new screening with values {} {} {}", movieToScreen, roomOfScreening, startOfScreening);
        screeningRepository.save(createScreeningInstance(movieToScreen, roomOfScreening, startOfScreening));

        return new ActionResult("Screening created", true);
    }

    @Override
    public boolean deleteScreening(String movieTitle, String roomName, LocalDateTime startOfScreening)
            throws UserNotLoggedInException, NotAuthorizedOperationException {

        authorizationService.userHasRoles(User.Role.ADMIN);

        Optional<Movie> movie = movieRepository.findById(movieTitle);
        Optional<Room> room = roomRepository.findById(roomName);

        if (movie.isEmpty() || room.isEmpty()) {
            return false;
        }

        Screening screeningToDelete = screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(movie.get(), room.get(), startOfScreening);

        logger.trace(screeningToDelete.toString());
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
