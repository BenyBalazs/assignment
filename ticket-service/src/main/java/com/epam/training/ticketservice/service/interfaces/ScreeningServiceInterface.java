package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.exception.OverlappingScreeningException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface ScreeningServiceInterface {

    boolean createScreening(String movieTitle, String roomName, LocalDateTime startOfScreening)
            throws OverlappingScreeningException;

    boolean deleteScreening(String movieTitle, String roomName, LocalDateTime startOfScreening);

    List<Screening> getAllScreening();
}
