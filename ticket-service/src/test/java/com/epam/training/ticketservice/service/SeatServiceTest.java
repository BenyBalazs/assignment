package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.entity.Room;
import com.epam.training.ticketservice.data.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SeatServiceTest {

    private SeatService seatService;
    @MockBean
    SeatRepository seatRepository;

    @BeforeEach
    public void setUp() {
        seatRepository = Mockito.mock(SeatRepository.class);
        seatService = new SeatService(seatRepository);
    }

    @Test
    public void testShouldReturnTheGivenSizeListWithTheSeatsInIt() {
        assertThat(seatService.createSeats(new Room(),10,10).size(), equalTo(100));
    }
}
