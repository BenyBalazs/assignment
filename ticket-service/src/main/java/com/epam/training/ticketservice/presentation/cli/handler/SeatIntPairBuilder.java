package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.utils.SeatIntPair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class SeatIntPairBuilder {

    public List<SeatIntPair> buildList(String seats) {

        return Arrays.stream(seats.split(" "))
                .map((x) -> {
                    var arr = x.split(",");
                    return createSeatIntPairFromString(arr[0], arr[1]);
                })
                .collect(Collectors.toList());

    }

    private SeatIntPair createSeatIntPairFromString(String row, String col) {

        return new SeatIntPair(Integer.parseInt(row), Integer.parseInt(col));
    }
}
