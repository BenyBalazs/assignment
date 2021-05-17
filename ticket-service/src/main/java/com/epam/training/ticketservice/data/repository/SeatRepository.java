package com.epam.training.ticketservice.data.repository;

import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    Seat findByRoomAndRowPositionAndColPosition(Room room, Integer rowPosition, Integer colPosition);
}
