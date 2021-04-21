package com.epam.training.ticketservice.commands.movie;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.MovieServiceInterface;

public class CreateMovieCommand implements Command {

    MovieServiceInterface movieService;
    String tile;
    String genre;
    int length;

    public CreateMovieCommand(MovieServiceInterface movieService,
                              String tile,
                              String genre,
                              int length) {
        this.movieService = movieService;
        this.tile = tile;
        this.genre = genre;
        this.length = length;
    }

    @Override
    public String execute() {

        try {
            if (movieService.createMovie(tile, genre, length)) {
                return "";
            } else {
                return "This movie is already created";
            }
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }

    }
}