package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.entity.BasePrice;
import com.epam.training.ticketservice.data.repository.BasePriceRepository;
import com.epam.training.ticketservice.service.interfaces.BasePriceServiceInterface;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class BasePriceService implements BasePriceServiceInterface {

    private final BasePriceRepository basePriceRepository;

    public BasePriceService(BasePriceRepository basePriceRepository) {
        this.basePriceRepository = basePriceRepository;
    }

    @Override
    public boolean updateBasePrice(int price) {
        try {
            BasePrice basePrice = basePriceRepository.findById("BasePrice").get();
            basePrice.setBasePrice(price);
            basePriceRepository.save(basePrice);
            return true;
        }catch (NoSuchElementException e) {
            persistIfNotFound(price);
            return false;
        }
    }

    @Override
    public int getBasePrice() {
        return basePriceRepository.findById("BasePrice").get().getBasePrice();
    }

    private void persistIfNotFound(int price) {
        BasePrice basePrice = new BasePrice("BasePrice", price);
        basePriceRepository.save(basePrice);
    }
}
