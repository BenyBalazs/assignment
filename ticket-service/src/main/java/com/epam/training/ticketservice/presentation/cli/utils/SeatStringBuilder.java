package com.epam.training.ticketservice.presentation.cli.utils;

import com.epam.training.ticketservice.utils.SeatIntPair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeatStringBuilder {

    public String buildSeatString(List<SeatIntPair> seats) {
        return seats.stream().map(SeatIntPair::toString).collect(Collectors.joining(", "));
    }
}
