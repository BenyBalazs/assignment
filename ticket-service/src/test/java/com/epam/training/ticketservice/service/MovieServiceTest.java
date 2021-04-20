package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Movie;
import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class MovieServiceTest {

    MovieService movieService;
    MovieRepository movieRepository;
    List<Movie> movieList;

    @BeforeEach
    public void setUp() {
        movieList = new LinkedList<>();
        movieRepository = Mockito.mock(MovieRepository.class);
        movieService = new MovieService(movieRepository);
        when(this.movieRepository.findById(Mockito.any(String.class)))
                .then( x -> movieList.stream().filter(y -> x.getArgument(0).equals(y.getTitle())).findFirst());

        doAnswer(x -> movieList.add(x.getArgument(0)))
                .when(movieRepository).save(Mockito.mock(Movie.class));

        doAnswer(x -> movieList.remove(x.getArgument(0)))
                .when(movieRepository).delete(Mockito.any(Movie.class));

        when(this.movieRepository.findAll()).then(x -> movieList);

    }

    @Test
    public void testShouldReturnTrueWhenTheOperationWasSuccessful() {
        assertThat(movieService.createMovie("asd","asd",120), equalTo(true));
    }

    @Test
    public void testShouldReturnFalseWhenMovieAlreadyExists() {
        movieList.add(new Movie("asd", "asd", 120, null));
        assertThat(movieService.createMovie("asd","asd",120), equalTo(false));
    }

    @Test
    public void testShouldReturnTrueWhenSuccessfullyModifyingAMovie() {
        movieList.add(new Movie("asd", "asd", 120, null));
        assertThat(movieService.modifyMovie("asd","asdasd",120), equalTo(true));
    }

    @Test
    public void testShouldReturnTrueWhenTryingToModifyNonExistingMovie() {
        movieList.add(new Movie("asd", "asd", 120, null));
        assertThat(movieService.modifyMovie("asdssss","asdasd",120), equalTo(false));
    }

    @Test
    public void testShouldReturnTrueWhenTryingToDeleteAMovieAndTheMovieIsInTheDatabase() {
        movieList.add(new Movie("asd", "asd", 120, null));
        assertThat(movieService.deleteMovie("asd"), equalTo(true));
    }

    @Test
    public void testShouldReturnTrueWhenTryingToDeleteNonExistingMovie() {
        movieList.add(new Movie("asd", "asd", 120, null));
        assertThat(movieService.deleteMovie("asdssss"), equalTo(false));
    }

}
