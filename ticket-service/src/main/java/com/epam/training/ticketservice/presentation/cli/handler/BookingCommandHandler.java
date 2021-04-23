package com.epam.training.ticketservice.presentation.cli.handler;

import com.epam.training.ticketservice.SeatIntPair;
import com.epam.training.ticketservice.commands.booking.BookASeatCommand;
import com.epam.training.ticketservice.service.interfaces.BookingServiceInterface;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class BookingCommandHandler {

    BookingServiceInterface bookingService;
    DateTimeFormatter dateTimeFormatter;

    public BookingCommandHandler(BookingServiceInterface bookingService, DateTimeFormatter dateTimeFormatter) {
        this.bookingService = bookingService;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @ShellMethod(value = "This method is used to book seats to screenings.", key = "book")
    public String bookASeat(String title, String roomName, String startOfScreening, String seats) {
        SeatIntPairBuilder builder = new SeatIntPairBuilder();
        
        BookASeatCommand bookASeatCommand 
                = new BookASeatCommand(bookingService, title, roomName,
                LocalDateTime.parse(startOfScreening, dateTimeFormatter), builder.buildList(seats));

        return bookASeatCommand.execute();
    }
}

class SeatIntPairBuilder {

    public List<SeatIntPair> buildList(String seats) {

        return Arrays.stream(seats.split(" "))
                .map((x) -> {var arr =x.split(",");
                return createSeatIntPairFromString(arr[0], arr[1]); })
                .collect(Collectors.toList());

    }

    private SeatIntPair createSeatIntPairFromString(String row, String col) {

        return new SeatIntPair(Integer.parseInt(row), Integer.parseInt(col));
    }
}
