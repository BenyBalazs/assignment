package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.data.entity.Movie;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieServiceInterface {

    boolean createMovie(String title, String genre, int length)
            throws UserNotLoggedInException, NotAuthorizedOperationException;

    boolean modifyMovie(String title, String genre, int length)
            throws UserNotLoggedInException, NotAuthorizedOperationException;

    List<Movie> getAllMoviesAsList();

    boolean deleteMovie(String title) throws UserNotLoggedInException, NotAuthorizedOperationException;
}
