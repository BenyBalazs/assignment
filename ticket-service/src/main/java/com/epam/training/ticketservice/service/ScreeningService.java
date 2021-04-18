package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.exception.OverlappingScreeningException;
import com.epam.training.ticketservice.service.interfaces.ScreeningServiceInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScreeningService implements ScreeningServiceInterface {

    ScreeningRepository screeningRepository;
    MovieRepository movieRepository;
    RoomRepository roomRepository;

    public ScreeningService(ScreeningRepository screeningRepository, MovieRepository movieRepository, RoomRepository roomRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public boolean createScreening(String movieTitle, String roomName, LocalDateTime startOfScreening)
            throws OverlappingScreeningException {

        Screening screeningToCreate;

        Movie movieToScreen = movieRepository.findById(movieTitle).orElse(null);

        if (movieToScreen == null) {
            return false;
        }
        Room roomOfScreening = roomRepository.findById(roomName).orElse(null);

        if (roomOfScreening == null) {
            return false;
        }

        if (canCreateScreening(startOfScreening, roomOfScreening)) {
            throw new OverlappingScreeningException();
        }

        screeningToCreate = new Screening();
        screeningToCreate.setMovie(movieToScreen);
        screeningToCreate.setRoomOfScreening(roomOfScreening);
        screeningToCreate.setStartOfScreening(startOfScreening);
        screeningToCreate.setEndOfScreening(calculateEndOfScreening(startOfScreening, movieToScreen.getLength()));

        screeningRepository.save(screeningToCreate);

        return true;
    }

    @Override
    public boolean deleteScreening(String movieTitle, String roomName, LocalDateTime startOfScreening) {
        return false;
    }

    @Override
    public List<Screening> getAllScreening() {
        return screeningRepository.findAll();
    }

    private boolean canCreateScreening(LocalDateTime startOfScreening, Room room) {
        List<Screening> screeningList = screeningRepository.findAll();
        return screeningList.stream().
                anyMatch(x -> x.getRoomOfScreening().equals(room) &&
                        x.getStartOfScreening().compareTo(startOfScreening) * startOfScreening.
                                compareTo(x.getEndOfScreening()) > 0);
    }

    private LocalDateTime calculateEndOfScreening(LocalDateTime start, int length) {
        return start.plusMinutes(length);
    }
}
