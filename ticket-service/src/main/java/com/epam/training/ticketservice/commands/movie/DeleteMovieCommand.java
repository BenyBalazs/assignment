package com.epam.training.ticketservice.commands.movie;

import com.epam.training.ticketservice.commands.Command;
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
        movieService.deleteMovie(title);
        return null;
    }
}
