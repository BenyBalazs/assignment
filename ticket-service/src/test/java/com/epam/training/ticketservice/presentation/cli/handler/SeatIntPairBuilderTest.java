package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.utils.SeatIntPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SeatIntPairBuilderTest {

    SeatIntPairBuilder underTest;

    @BeforeEach
    public void setUp() {
        underTest = new SeatIntPairBuilder();
    }

    @Test
    public void testBuildListShouldReturnSeatIntPairListWithTheProperValues() {
        String seats = "3,4 5,6 7,8 6,6";

        List<SeatIntPair> expected = new ArrayList<>();
        expected.add(new SeatIntPair(3,4));
        expected.add(new SeatIntPair(5,6));
        expected.add(new SeatIntPair(7,8));
        expected.add(new SeatIntPair(6,6));

        assertThat(underTest.buildList(seats), equalTo(expected));

    }
}
