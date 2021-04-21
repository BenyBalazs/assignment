package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.MovieServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService implements MovieServiceInterface {

    private MovieRepository movieRepository;
    private AuthorizationService authorizationService;

    public MovieService(MovieRepository movieRepository, AuthorizationService authorizationService) {
        this.movieRepository = movieRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean createMovie(String title, String genre, int length) throws UserNotLoggedInException, NotAuthorizedOperationException {

        authorizationService.userHasRoles(User.Role.ADMIN);

        if (!doesTheEntityExists(title)) {
            return false;
        }

        Movie movieToCreate = new Movie();
        movieToCreate.setTitle(title);
        movieToCreate.setGenre(genre);
        movieToCreate.setLength(length);

        movieRepository.save(movieToCreate);

        return true;
    }

    @Override
    public boolean modifyMovie(String title, String genre, int length) throws UserNotLoggedInException, NotAuthorizedOperationException {

        authorizationService.userHasRoles(User.Role.ADMIN);

        Movie movieToModify = findMovie(title);

        if (doesTheEntityExists(title)) {
            return false;
        }

        movieToModify.setGenre(genre);
        movieToModify.setLength(length);

        movieRepository.save(movieToModify);

        return true;
    }

    @Override
    public List<Movie> getAllMoviesAsList() {
        return movieRepository.findAll();
    }

    @Override
    public boolean deleteMovie(String title) throws UserNotLoggedInException, NotAuthorizedOperationException {

        authorizationService.userHasRoles(User.Role.ADMIN);

        Movie movieToDelete = findMovie(title);

        if (doesTheEntityExists(title)) {
            return false;
        }

        movieRepository.delete(movieToDelete);

        return true;
    }

    private boolean doesTheEntityExists(String title) {

        Movie movieToCreate = findMovie(title);

        return movieToCreate == null;
    }
    private Movie findMovie(String title) {
        return movieRepository.findById(title).orElse(null);
    }
}
