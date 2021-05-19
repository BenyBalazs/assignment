package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.utils.ActionResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface PriceComponentServiceInterface {

     ActionResult attachPriceComponentToRoom(String roomName, String priceComponentName);

     ActionResult attachPriceComponentToMovie(String title, String priceComponentName);

     ActionResult attachPriceComponentToScreening(String title, String roomName, LocalDateTime timeOfScreening,
                                                  String priceComponentName);

     boolean createPriceComponent(String name, int price);

}
