package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.utils.ActionResult;
import org.springframework.stereotype.Service;

@Service
public interface PriceComponentService {

     ActionResult attachPriceComponentToRoom(String roomName, String priceComponentName);

     ActionResult attachPriceComponentToMovie(String title, String priceComponentName);

}
