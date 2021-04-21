package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.commands.movie.CreateMovieCommand;
import com.epam.training.ticketservice.commands.movie.DeleteMovieCommand;
import com.epam.training.ticketservice.commands.movie.ListMoviesCommand;
import com.epam.training.ticketservice.commands.movie.ModifyMovieCommand;
import com.epam.training.ticketservice.service.interfaces.MovieServiceInterface;
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
        CreateMovieCommand command = new CreateMovieCommand(movieService, title, genre, length);
        return command.execute();
    }

    @ShellMethod(value = "This command is used to modify existing movies", key = "update movie")
    public String updateMovie(String title, String genre, int length) {
        ModifyMovieCommand command = new ModifyMovieCommand(movieService, title, genre, length);
        return command.execute();
    }

    @ShellMethod(value = "This command is used to delete existing movies", key = "delete movie")
    public String deleteMovie(String title) {
        DeleteMovieCommand command = new DeleteMovieCommand(movieService, title);
        return command.execute();
    }

    @ShellMethod(value = "This command is used to list all movies.", key = "list movies")
    public String listMovies() {
        ListMoviesCommand command = new ListMoviesCommand(movieService);
        return command.execute();
    }

}
