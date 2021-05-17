package com.epam.training.ticketservice.data.dto;

import com.epam.training.ticketservice.data.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MovieTest {

    private Movie movie;

    @BeforeEach
    public void setUp() {
        movie = new Movie("Pestis", "asd", 120,null);
    }

    @Test
    public void testToStringShouldReturnFormattedString() {
        assertThat(movie.toString(), equalTo("Pestis (asd, 120 minutes)"));
    }

}
