package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.entity.BasePrice;
import com.epam.training.ticketservice.data.repository.BasePriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class BasePriceServiceTest {

    BasePriceService underTest;
    @MockBean
    BasePriceRepository basePriceRepository;

    @BeforeEach
    public void setUp() {
        basePriceRepository = Mockito.mock(BasePriceRepository.class);
        underTest = new BasePriceService(basePriceRepository);
    }

    @Test
    public void testGetBasePriceShouldThrowNoSuchElementExceptionWhenPriceDoesNotExist() {
        when(basePriceRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> underTest.getBasePrice());
    }

    @Test
    public void testUpdateBasePriceShouldReturnFalseWhenBasePriceDoesNotExist() {
        when(basePriceRepository.findById(any(String.class))).thenReturn(Optional.empty());

        boolean actual = underTest.updateBasePrice(1500);

        assertThat(actual, equalTo(false));
    }

    @Test
    public void testUpdateBasePriceShouldReturnTrueWhenBasePriceDoesExist() {
        when(basePriceRepository.findById(any(String.class))).thenReturn(Optional.of(new BasePrice("BasePrice", 1500)));

        boolean actual = underTest.updateBasePrice(1600);

        assertThat(actual, equalTo(true));
    }

    @Test
    public void testGetBasePriceShouldTReturnTheBasePriceWhenItExists() {
        when(basePriceRepository.findById(any(String.class))).thenReturn(Optional.of(new BasePrice("BasePrice", 1500)));

        int actual = underTest.getBasePrice();

        assertThat(actual, equalTo(1500));
    }
}
