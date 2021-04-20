package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.exception.OverlappingScreeningException;
import com.epam.training.ticketservice.exception.ScreeningInTheBrakePeriodException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface ScreeningServiceInterface {

    boolean createScreening(String movieTitle, String roomName, LocalDateTime startOfScreening)
            throws OverlappingScreeningException, ScreeningInTheBrakePeriodException;

    boolean deleteScreening(String movieTitle, String roomName, LocalDateTime startOfScreening);

    List<Screening> getAllScreening();
}
