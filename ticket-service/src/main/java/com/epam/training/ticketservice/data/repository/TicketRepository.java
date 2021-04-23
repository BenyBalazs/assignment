package com.epam.training.ticketservice.data.repository;

import com.epam.training.ticketservice.data.dao.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
