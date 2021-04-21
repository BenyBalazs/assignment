package com.epam.training.ticketservice.commands.movie;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.MovieService;

public class ModifyMovieCommand implements Command {
    MovieService movieService;
    String title;
    String genre;
    int length;

    public ModifyMovieCommand(MovieService movieService, String title, String genre, int length) {
        this.movieService = movieService;
        this.title = title;
        this.genre = genre;
        this.length = length;
    }

    @Override
    public String execute() {
        try {
            movieService.modifyMovie(title, genre, length);
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
        return null;
    }
}
