package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.data.entity.Ticket;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.data.repository.TicketRepository;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.BookingServiceInterface;
import com.epam.training.ticketservice.service.user.AuthorizationService;
import com.epam.training.ticketservice.utils.BookingActionResult;
import com.epam.training.ticketservice.utils.SeatIntPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService implements BookingServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger("ScreeningService.class");

    private final AuthorizationService authorizationService;
    private final ScreeningRepository screeningRepository;
    private final TicketRepository ticketRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final BookingServiceHelper bookingServiceHelper;

    public BookingService(AuthorizationService authorizationService,
                          ScreeningRepository screeningRepository,
                          TicketRepository ticketRepository,
                          RoomRepository roomRepository,
                          MovieRepository movieRepository,
                          BookingServiceHelper bookingServiceHelper) {
        this.authorizationService = authorizationService;
        this.screeningRepository = screeningRepository;
        this.ticketRepository = ticketRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.bookingServiceHelper = bookingServiceHelper;
    }

    @Override
    public BookingActionResult bookSeat(String movieTitle,
                                        String roomName,
                                        LocalDateTime startOfScreening,
                                        List<SeatIntPair> seatsToBook) throws UserNotLoggedInException {

        authorizationService.userIsLoggedIn();

        Movie screenedMovie = movieRepository.findById(movieTitle).orElse(null);
        Room roomOfScreening = roomRepository.findById(roomName).orElse(null);
        Screening screening = screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(screenedMovie, roomOfScreening, startOfScreening);

        BookingActionResult nullCheckResult = nullChecker(screenedMovie, roomOfScreening, screening);

        if (!nullCheckResult.isSuccess()) {
            return nullCheckResult;
        }

        BookingActionResult ticketActionResult
                = bookingServiceHelper.setTicketsToSave(seatsToBook, screening, roomOfScreening);

        List<Ticket> ticketsToSave = bookingServiceHelper.getTicketsToSave();

        if (!ticketActionResult.isSuccess()) {
            return ticketActionResult;
        }

        for (var tickets: ticketsToSave) {
            ticketRepository.save(tickets);
        }

        return new BookingActionResult("SeatsBooked", true,
                bookingServiceHelper.calculateTicketPrice(ticketsToSave));
    }

    private BookingActionResult nullChecker(Movie movieToCheck, Room roomToCheck, Screening screeningToCheck) {

        if (movieToCheck == null) {
            return new BookingActionResult("NoMovie", false);
        } else if (roomToCheck == null) {
            return new BookingActionResult("NoRoom", false);
        } else if (screeningToCheck == null) {
            return new BookingActionResult("NoScreening", false);
        }
        return new BookingActionResult("NoNullValue", true);
    }



}
