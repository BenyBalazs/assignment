package com.epam.training.ticketservice.commands.movie;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.Lister;
import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.service.interfaces.MovieServiceInterface;

public class ListMoviesCommand implements Command {

    private final Lister<Movie> lister;

    public ListMoviesCommand(MovieServiceInterface movieService) {
        lister = new Lister<Movie>("There are no movies at the moment", movieService.getAllMoviesAsList());
    }

    @Override
    public String execute() {
        return lister.list();
    }
}
