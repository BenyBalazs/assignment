package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import com.epam.training.ticketservice.service.interfaces.MovieServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService implements MovieServiceInterface {

    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public boolean createMovie(String title, String genre, int length) {

        if (movieRepository.findById(title).isPresent()) {
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
    public boolean modifyRoom(String title, String genre, int length) {

        Movie movieToModify = movieRepository.findById(title).orElse(null);

        if (movieToModify == null) {
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
    public boolean deleteRoom(String title) {

        Movie movieToDelete = movieRepository.findById(title).orElse(null);

        if (movieToDelete == null) {
            return false;
        }

        movieRepository.delete(movieToDelete);

        return true;
    }
}
