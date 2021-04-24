package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.utils.BookingActionResult;
import com.epam.training.ticketservice.utils.SeatIntPair;
import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.dao.Seat;
import com.epam.training.ticketservice.data.dao.Ticket;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.data.repository.SeatRepository;
import com.epam.training.ticketservice.data.repository.TicketRepository;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.BookingServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.rmi.AlreadyBoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService implements BookingServiceInterface {

    private static final int DEFAULT_PRICE = 1500;
    private static final Logger logger = LoggerFactory.getLogger("ScreeningService.class");

    private final SeatRepository seatRepository;
    private final ActiveUserStore activeUserStore;
    private final AuthorizationService authorizationService;
    private final ScreeningRepository screeningRepository;
    private final TicketRepository ticketRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;

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
    public BookingActionResult bookSeat(String movieTitle,
                                        String roomName,
                                        LocalDateTime startOfScreening,
                                        List<SeatIntPair> seatsToBook) throws UserNotLoggedInException {

        authorizationService.userIsLoggedIn();

        Movie screenedMovie = movieRepository.findById(movieTitle).orElse(null);
        Room roomOfScreening = roomRepository.findById(roomName).orElse(null);
        Screening screening = screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(screenedMovie, roomOfScreening, startOfScreening);

        //System.out.println(screenedMovie);
        //System.out.println(roomOfScreening);
        //System.out.println(screening);
        BookingActionResult nullCheckResult = nullChecker(screenedMovie, roomOfScreening, screening);

        if (!nullCheckResult.isSuccess()) {
            return nullCheckResult;
        }

        List<Ticket> ticketsToSave = new ArrayList<>();
        BookingActionResult ticketActionResult
                = setTicketsToSave(ticketsToSave, seatsToBook, screening, roomOfScreening);

        if (!ticketActionResult.isSuccess()) {
            return ticketActionResult;
        }

        for (var tickets: ticketsToSave) {
            ticketRepository.save(tickets);
        }

        return new BookingActionResult("SeatsBooked", true, calculateTicketPrice(ticketsToSave));
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

    private BookingActionResult setTicketsToSave(List<Ticket> ticketsToSave, List<SeatIntPair> seatsToBook,
                                                 Screening screening, Room roomOfScreening) {
        Ticket ticketToVerify;
        SeatIntPair seatIntPair = null;
        try {
            for (var seat: seatsToBook) {
                seatIntPair = seat;
                ticketToVerify = crateTicketIfPossible(screening, roomOfScreening, seat.getRow(), seat.getColumn());
                if (ticketToVerify == null) {
                    return new BookingActionResult("NoSeat", false, seatIntPair);
                }
                ticketsToSave.add(ticketToVerify);
            }
        } catch (AlreadyBoundException e) {
            return new BookingActionResult("Taken", false, seatIntPair);
        }

        return new BookingActionResult("ticketToSaveListIsSet", true);
    }

    private Ticket crateTicketIfPossible(Screening screening, Room room, int row, int col)
            throws AlreadyBoundException {

        Seat seat = seatRepository.findByRoomAndRowPositionAndColPosition(room, row, col);

        if (seat == null) {
            return null;
        }

        if (ticketRepository.findBySeatAndScreening(seat, screening) != null) {
            throw new AlreadyBoundException("Seat taken");
        }

        return createTicketInstance(screening, seat);
    }

    private Ticket createTicketInstance(Screening screening, Seat seat) {
        Ticket ticket = new Ticket();
        ticket.setTicketPrice(DEFAULT_PRICE);
        ticket.setSeat(seat);
        ticket.setUser(activeUserStore.getActiveUser());
        ticket.setScreening(screening);
        return ticket;
    }

    private int calculateTicketPrice(List<Ticket> tickets) {
        return tickets.stream().map(Ticket::getTicketPrice).reduce(Integer::sum).orElse(0);
    }

}
