package com.epam.training.ticketservice.service.utils;

import com.epam.training.ticketservice.data.entity.PriceComponent;
import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.data.entity.Seat;
import com.epam.training.ticketservice.service.BasePriceService;
import org.springframework.stereotype.Component;

@Component
public class PriceCalculator {

    BasePriceService basePriceService;

    public PriceCalculator(BasePriceService basePriceService) {
        this.basePriceService = basePriceService;
    }

    public int calculateTicketPrice(Screening screening, Seat seat) {
        int priceToReturn = basePriceService.getBasePrice();
        priceToReturn += screening.getMovie()
                .getAttachedComponents().stream().map(PriceComponent::getPrice).reduce(Integer::sum).orElse(0);
        priceToReturn += screening
                .getAttachedComponents().stream().map(PriceComponent::getPrice).reduce(Integer::sum).orElse(0);
        priceToReturn += seat.getRoom()
                .getAttachedComponents().stream().map(PriceComponent::getPrice).reduce(Integer::sum).orElse(0);

        return priceToReturn;
    }
}
