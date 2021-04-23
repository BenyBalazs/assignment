package com.epam.training.ticketservice.utils;

import java.util.List;
import java.util.stream.Collectors;

public class Lister<T> {

    String whenEmpty;
    List<T> data;

    public Lister(String whenEmpty, List<T> data) {
        this.whenEmpty = whenEmpty;
        this.data = data;
    }

    public String list() {
        if (data.isEmpty()) {
            return whenEmpty;
        }
        return data.stream().map(T::toString).collect(Collectors.joining("\n"));
    }
}
