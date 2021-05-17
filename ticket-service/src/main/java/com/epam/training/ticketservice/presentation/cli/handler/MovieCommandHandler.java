package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.MovieServiceInterface;
import com.epam.training.ticketservice.utils.Lister;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MovieCommandHandler {

    MovieServiceInterface movieService;

    public MovieCommandHandler(MovieServiceInterface movieService) {
        this.movieService = movieService;
    }

    @ShellMethod(value = "This is used to persist new movies to the database", key = "create movie")
    public String createMovie(String title, String genre, int length) {
        try {
            if (movieService.createMovie(title, genre, length)) {
                return "";
            } else {
                return "This movie is already created";
            }
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "This command is used to modify existing movies", key = "update movie")
    public String updateMovie(String title, String genre, int length) {
        try {
            movieService.modifyMovie(title, genre, length);
        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
        return "Movie modified.";
    }

    @ShellMethod(value = "This command is used to delete existing movies", key = "delete movie")
    public String deleteMovie(String title) {
        try {
            if (movieService.deleteMovie(title)) {
                return "Movie deleted.";
            } else {
                return "No movie found";
            }

        } catch (UserNotLoggedInException | NotAuthorizedOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "This command is used to list all movies.", key = "list movies")
    public String listMovies() {
        Lister<Movie> lister =
                new Lister<Movie>("There are no movies at the moment", movieService.getAllMoviesAsList());

        return lister.list();
    }

}
