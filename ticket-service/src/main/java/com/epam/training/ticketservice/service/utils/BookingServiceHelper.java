package com.epam.training.ticketservice.service.utils;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.data.entity.Seat;
import com.epam.training.ticketservice.data.entity.Ticket;
import com.epam.training.ticketservice.data.repository.SeatRepository;
import com.epam.training.ticketservice.data.repository.TicketRepository;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.utils.BookingActionResult;
import com.epam.training.ticketservice.utils.SeatIntPair;
import org.springframework.stereotype.Component;

import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookingServiceHelper {

    private static final int DEFAULT_PRICE = 1500;

    private final ActiveUserStore activeUserStore;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final PriceCalculator priceCalculator;

    private List<Ticket> ticketsToSave;

    public BookingServiceHelper(ActiveUserStore activeUserStore,
                                SeatRepository seatRepository,
                                TicketRepository ticketRepository,
                                PriceCalculator priceCalculator) {
        this.activeUserStore = activeUserStore;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.priceCalculator = priceCalculator;
        this.ticketsToSave = new ArrayList<>();
    }

    public BookingActionResult setTicketsToSave(List<SeatIntPair> seatsToBook,
                                                Screening screening, Room roomOfScreening) {
        ticketsToSave = new ArrayList<>();

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

    public Ticket crateTicketIfPossible(Screening screening, Room room, int row, int col)
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

    public Ticket createTicketInstance(Screening screening, Seat seat) {
        Ticket ticket = new Ticket();
        ticket.setTicketPrice(priceCalculator.calculateTicketPrice(screening, seat));
        ticket.setSeat(seat);
        ticket.setUser(activeUserStore.getActiveUser());
        ticket.setScreening(screening);
        return ticket;
    }

    public int calculateTicketPrice(List<Ticket> tickets) {
        return tickets.stream().map(Ticket::getTicketPrice).reduce(0, Integer::sum);
    }

    public BookingActionResult nullChecker(Movie movieToCheck, Room roomToCheck, Screening screeningToCheck) {

        if (movieToCheck == null) {
            return new BookingActionResult("NoMovie", false);
        } else if (roomToCheck == null) {
            return new BookingActionResult("NoRoom", false);
        } else if (screeningToCheck == null) {
            return new BookingActionResult("NoScreening", false);
        }
        return new BookingActionResult("NoNullValue", true);
    }

    public List<Ticket> getTicketsToSave() {
        return ticketsToSave;
    }


}
