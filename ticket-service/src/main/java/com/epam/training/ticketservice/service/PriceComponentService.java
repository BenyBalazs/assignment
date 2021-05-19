package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.data.entity.PriceComponent;
import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.data.repository.PriceComponentRepository;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.ScreeningRepository;
import com.epam.training.ticketservice.service.interfaces.PriceComponentServiceInterface;
import com.epam.training.ticketservice.utils.ActionResult;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class PriceComponentService implements PriceComponentServiceInterface {

    private static final ActionResult SUCCESS_RESULT = new ActionResult("ComponentAttached", true);
    private static final ActionResult NO_PRICE_COMPONENT = new ActionResult("NoPriceComponent", false);
    private static final ActionResult NO_MOVE_RESULT = new ActionResult("NoMovie", false);
    private static final ActionResult NO_ROOM_RESULT  = new ActionResult("NoRoom", false);
    private static final ActionResult NO_SCREENING_RESULT = new ActionResult("NoScreening", false);

    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final PriceComponentRepository priceComponentRepository;
    private final RoomRepository roomRepository;

    public PriceComponentService(MovieRepository movieRepository,
                                 ScreeningRepository screeningRepository,
                                 PriceComponentRepository priceComponentRepository,
                                 RoomRepository roomRepository) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.priceComponentRepository = priceComponentRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public ActionResult attachPriceComponentToRoom(String roomName, String priceComponentName) {

        PriceComponent componentToAttach;

        try {
            componentToAttach = priceComponentRepository.findById(priceComponentName).get();
        }catch (NoSuchElementException e) {
            return NO_PRICE_COMPONENT;
        }

        Room roomToAttach = roomRepository.findById(roomName).orElse(null);

        if (roomToAttach == null) {
            return NO_ROOM_RESULT;
        }

        componentToAttach.getAttachedRooms().add(roomToAttach);

        priceComponentRepository.save(componentToAttach);

        return SUCCESS_RESULT;
    }

    @Override
    public ActionResult attachPriceComponentToMovie(String title, String priceComponentName) {

        PriceComponent componentToAttach;

        try {
            componentToAttach = priceComponentRepository.findById(priceComponentName).get();
        }catch (NoSuchElementException e) {
            return NO_PRICE_COMPONENT;
        }

        Movie movieToAttach = movieRepository.findById(title).orElse(null);

        if (movieToAttach == null) {
            return NO_MOVE_RESULT;
        }

        componentToAttach.getAttachedMovies().add(movieToAttach);

        priceComponentRepository.save(componentToAttach);

        return SUCCESS_RESULT;
    }

    @Override
    public ActionResult attachPriceComponentToScreening(String title,
                                                        String roomName,
                                                        LocalDateTime timeOfScreening,
                                                        String priceComponentName) {
        Screening screeningToAttach = findScreening(title, roomName, timeOfScreening);

        if(screeningToAttach == null) {
            return NO_SCREENING_RESULT;
        }

        PriceComponent componentToAttach;

        try {
            componentToAttach = priceComponentRepository.findById(priceComponentName).get();
        }catch (NoSuchElementException e) {
            return NO_PRICE_COMPONENT;
        }

        componentToAttach.getAttachedScreenings().add(screeningToAttach);

        priceComponentRepository.save(componentToAttach);

        return SUCCESS_RESULT;

    }

    @Override
    public boolean createPriceComponent(String name, int price) {
        try {
            priceComponentRepository.findById(name).get();
            return false;
        }catch (NoSuchElementException e) {
            PriceComponent priceComponentToSave = new PriceComponent();
            priceComponentToSave.setPrice(price);
            priceComponentToSave.setPriceComponentName(name);
            priceComponentRepository.save(priceComponentToSave);
            return true;
        }
    }

    private Screening findScreening(String title,
                                    String roomName,
                                    LocalDateTime timeOfScreening) {

        try{
            Movie movie = movieRepository.findById(title).get();
            Room roomOfScreening = roomRepository.findById(roomName).get();
            return screeningRepository.
                    findByMovieAndRoomOfScreeningAndStartOfScreening(movie, roomOfScreening, timeOfScreening);
        }catch (NoSuchElementException e) {
            return null;
        }

    }

}
