package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.service.BasePriceService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class BasePriceHandler {

    private final BasePriceService basePriceService;

    public BasePriceHandler(BasePriceService basePriceService) {
        this.basePriceService = basePriceService;
    }

    @ShellMethod(value = "This method is used to update the base price of every ticket.", key = "update base price")
    public String updatePrice(int price) {
        basePriceService.updateBasePrice(price);
        return "The new base price is: " + price;
    }
}
