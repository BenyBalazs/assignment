package com.epam.training.ticketservice.service.utils;

import com.epam.training.ticketservice.data.entity.PriceComponent;
import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.data.entity.Seat;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.service.BasePriceService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PriceCalculator {

    private final BasePriceService basePriceService;
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final RoomRepository roomRepository;

    public PriceCalculator(BasePriceService basePriceService,
                           MovieRepository movieRepository,
                           ScreeningRepository screeningRepository,
                           RoomRepository roomRepository) {
        this.basePriceService = basePriceService;
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.roomRepository = roomRepository;
    }

    public int calculateTicketPrice(Screening screening, Seat seat) {
        int priceToReturn = basePriceService.getBasePrice();

        List<PriceComponent> moviePriceComponents = movieRepository.findById(screening.getMovie().getTitle()).get()
                .getAttachedComponents();
        priceToReturn += moviePriceComponents.stream()
                .map(PriceComponent::getPrice).reduce(Integer::sum).orElse(0);
        List<PriceComponent> screeningComponents = screeningRepository
                .findByMovieAndRoomOfScreeningAndStartOfScreening(screening.getMovie(),
                        screening.getRoomOfScreening(), screening.getStartOfScreening())
                .getAttachedComponents();
        priceToReturn += screeningComponents.stream()
                .map(PriceComponent::getPrice).reduce(Integer::sum).orElse(0);
        List<PriceComponent> roomPriceComponent = roomRepository.findById(seat.getRoom().getRoomName())
                .get().getAttachedComponents();
        priceToReturn += roomPriceComponent.stream().map(PriceComponent::getPrice).reduce(Integer::sum).orElse(0);

        return priceToReturn;
    }
}
