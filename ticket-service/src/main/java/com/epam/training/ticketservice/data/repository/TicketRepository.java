package com.epam.training.ticketservice.data.repository;

import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.data.entity.Seat;
import com.epam.training.ticketservice.data.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Ticket findBySeatAndScreening(Seat seat, Screening screening);
}
