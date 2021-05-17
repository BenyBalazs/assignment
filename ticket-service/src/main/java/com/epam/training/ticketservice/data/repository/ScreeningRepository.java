package com.epam.training.ticketservice.data.repository;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    Screening findByMovieAndRoomOfScreeningAndStartOfScreening(Movie movie, Room room, LocalDateTime startOfScreening);

    List<Screening> findAllByRoomOfScreening(Room roomOfScreening);
}
