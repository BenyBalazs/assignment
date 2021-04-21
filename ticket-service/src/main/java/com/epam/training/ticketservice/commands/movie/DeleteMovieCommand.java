package com.epam.training.ticketservice.commands.movie;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.MovieServiceInterface;

public class DeleteMovieCommand implements Command {

    MovieServiceInterface movieService;
    String title;

    public DeleteMovieCommand(MovieServiceInterface movieService, String title) {
        this.movieService = movieService;
        this.title = title;
    }

    @Override
    public String execute() {
        try {
            movieService.deleteMovie(title);
            return "";
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
    }
}
