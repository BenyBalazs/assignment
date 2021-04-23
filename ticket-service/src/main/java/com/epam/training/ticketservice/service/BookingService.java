package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.ActiveUserStore;
import com.epam.training.ticketservice.BookingActionResult;
import com.epam.training.ticketservice.SeatIntPair;
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
import org.springframework.stereotype.Service;

import java.rmi.AlreadyBoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService implements BookingServiceInterface {

    private static final int DEFAULT_PRICE = 1500;


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
    public BookingActionResult bookSeat(String movieTitle,
                                        String roomName,
                                        LocalDateTime startOfScreening,
                                        List<SeatIntPair> seatsToBook) throws UserNotLoggedInException {

        authorizationService.userIsLoggedIn();

        Movie movie = movieRepository.findById(movieTitle).orElse(null);

        if (movie == null) {
            return new BookingActionResult("NoMovie", false);
        }

        Room room = roomRepository.findById(roomName).orElse(null);

        if (room == null) {
            return new BookingActionResult("NoRoom", false);
        }

        Screening screening = screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(movie, room, startOfScreening);

        if (screening == null) {
            return new BookingActionResult("NoScreening", false);
        }

        List<Ticket> ticketsToSave = new ArrayList<>();
        Ticket ticket;
        SeatIntPair seatIntPair = null;
        try {
            for (var seat: seatsToBook) {
                ticket = crateTicketIfPossible(screening, room, seat.getRow(), seat.getColumn());
                seatIntPair = seat;
                if (ticket == null) {
                    return new BookingActionResult("NoSeat", false, seat);
                }
                ticketsToSave.add(ticket);
            }
        } catch (AlreadyBoundException e) {
            return new BookingActionResult("Taken", false, seatIntPair);
        }

        for (var tickets: ticketsToSave) {
            ticketRepository.save(tickets);
        }

        return new BookingActionResult("SeatsBooked", true, calculateTicketPrice(ticketsToSave));
    }

    private List<Ticket> createTicketListToSave(Screening screening, Room room, List<SeatIntPair> seatsToBook)
            throws AlreadyBoundException {
        List<Ticket> ticketsToReturn = new ArrayList<>();

        for (var seat: seatsToBook) {
            Ticket ticketToAdd = crateTicketIfPossible(screening, room, seat.getRow(), seat.getColumn());
            if (ticketToAdd == null) {
                return null;
            }
            ticketsToReturn.add(ticketToAdd);
        }

        return ticketsToReturn;

    }

    private Ticket crateTicketIfPossible(Screening screening, Room room, int row, int col)
            throws AlreadyBoundException {

        Seat seat = seatRepository.findByRoomAndRowPositionAndColPosition(room, row, col);

        if (seat == null ) {
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
        return tickets.stream().map(Ticket::getTicketPrice).reduce(Integer::sum).orElse(null);
    }

}
