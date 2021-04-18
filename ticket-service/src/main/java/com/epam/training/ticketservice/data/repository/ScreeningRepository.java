package com.epam.training.ticketservice.data.repository;

import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.dao.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
@Transactional
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    Screening findByMovieAndRoomOfScreeningAndStartOfScreening(Movie movie, Room room, LocalDateTime startOfScreening);
    Screening findByStartOfScreening(LocalDateTime startOfScreening);
}
