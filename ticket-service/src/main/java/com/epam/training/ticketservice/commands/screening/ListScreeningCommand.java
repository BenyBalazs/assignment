package com.epam.training.ticketservice.commands.screening;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.Lister;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.service.interfaces.ScreeningServiceInterface;

public class ListScreeningCommand implements Command {

    private final Lister<Screening> screeningLister;

    public ListScreeningCommand(ScreeningServiceInterface screeningService) {
        screeningLister = new Lister<>("There are no screenings", screeningService.getAllScreening());
    }

    @Override
    public String execute() {
        return screeningLister.list();
    }
}
