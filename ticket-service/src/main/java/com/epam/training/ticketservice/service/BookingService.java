package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.ActionResult;
import com.epam.training.ticketservice.ActiveUserStore;
import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.data.repository.SeatRepository;
import com.epam.training.ticketservice.data.repository.TicketRepository;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.BookingServiceInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService implements BookingServiceInterface {

    SeatRepository seatRepository;
    ActiveUserStore activeUserStore;
    AuthorizationService authorizationService;
    ScreeningRepository screeningRepository;
    TicketRepository ticketRepository;
    RoomRepository roomRepository;
    MovieRepository movieRepository;

    public BookingService(SeatRepository seatRepository,
                          ActiveUserStore activeUserStore,
                          AuthorizationService authorizationService,
                          ScreeningRepository screeningRepository,
                          TicketRepository ticketRepository,
                          RoomRepository roomRepository,
                          MovieRepository movieRepository) {
        this.seatRepository = seatRepository;
        this.activeUserStore = activeUserStore;
        this.authorizationService = authorizationService;
        this.screeningRepository = screeningRepository;
        this.ticketRepository = ticketRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public ActionResult bookSeat(String movieTitle,
                                 String roomName,
                                 LocalDateTime startOfScreening,
                                 List<String> seatsToBook) throws UserNotLoggedInException {

        authorizationService.userIsLoggedIn();

        Movie movie = movieRepository.findById(movieTitle).orElse(null);

        if (movie == null) {
            return new ActionResult("NoMovie", false);
        }

        Room room = roomRepository.findById(roomName).orElse(null);

        if (room == null) {
            return new ActionResult("NoRoom", false);
        }

        Screening screening = screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(movie, room, startOfScreening);

        if (screening == null) {
            return new ActionResult("NoScreening", false);
        }


        return null;
    }

}
