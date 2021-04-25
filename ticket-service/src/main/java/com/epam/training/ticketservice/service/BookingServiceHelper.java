package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.dao.Seat;
import com.epam.training.ticketservice.data.dao.Ticket;
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

    private List<Ticket> ticketsToSave;

    public BookingServiceHelper(ActiveUserStore activeUserStore,
                                SeatRepository seatRepository,
                                TicketRepository ticketRepository) {
        this.activeUserStore = activeUserStore;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.ticketsToSave = new ArrayList<>();
    }

    public BookingActionResult setTicketsToSave(List<SeatIntPair> seatsToBook,
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
        ticket.setTicketPrice(DEFAULT_PRICE);
        ticket.setSeat(seat);
        ticket.setUser(activeUserStore.getActiveUser());
        ticket.setScreening(screening);
        return ticket;
    }

    public int calculateTicketPrice(List<Ticket> tickets) {
        return tickets.stream().map(Ticket::getTicketPrice).reduce(Integer::sum).orElse(0);
    }

    public List<Ticket> getTicketsToSave() {
        return ticketsToSave;
    }
}
