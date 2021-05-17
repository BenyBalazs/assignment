package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.utils.ActionResult;
import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface ScreeningServiceInterface {

    ActionResult createScreening(String movieTitle, String roomName, LocalDateTime startOfScreening)
            throws UserNotLoggedInException, NotAuthorizedOperationException;

    boolean deleteScreening(String movieTitle, String roomName, LocalDateTime startOfScreening)
            throws UserNotLoggedInException, NotAuthorizedOperationException;

    List<Screening> getAllScreening();
}
