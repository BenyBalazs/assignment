package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.data.dao.Movie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieServiceInterface {

    boolean createMovie(String title, String genre, int length);

    boolean modifyMovie(String title, String genre, int length);

    List<Movie> getAllMoviesAsList();

    boolean deleteMovie(String title);
}
