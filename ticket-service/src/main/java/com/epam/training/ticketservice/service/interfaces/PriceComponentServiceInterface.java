package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.utils.ActionResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface PriceComponentServiceInterface {

    ActionResult attachPriceComponentToRoom(String roomName, String priceComponentName)
            throws UserNotLoggedInException, NotAuthorizedOperationException;

    ActionResult attachPriceComponentToMovie(String title, String priceComponentName)
            throws UserNotLoggedInException, NotAuthorizedOperationException;

    ActionResult attachPriceComponentToScreening(String title, String roomName, LocalDateTime timeOfScreening,
                                                  String priceComponentName)
            throws UserNotLoggedInException, NotAuthorizedOperationException;

    boolean createPriceComponent(String name, int price)
            throws UserNotLoggedInException, NotAuthorizedOperationException;

}
