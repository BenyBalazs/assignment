package com.epam.training.ticketservice.presentation.cli.utils;

import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.dao.Seat;
import com.epam.training.ticketservice.data.dao.Ticket;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingStringBuilder {

    private final DateTimeFormatter dateTimeFormatter;

    public BookingStringBuilder(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public String buildBookingString(List<Ticket> ticketList) {

        if (ticketList.isEmpty()) {
            return "You have not booked any tickets yet";
        }
        List<Screening> distinctScreenings = ticketList.stream()
                .map(Ticket::getScreening).distinct().collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("Your previous bookings are");
        sb.append("\n");
        for (var screening : distinctScreenings) {

            sb.append("Seats ");

            sb.append(ticketList.stream()
                    .filter(x -> x.getScreening().equals(screening))
                    .map(Ticket::getSeat).map(Seat::toString)
                    .collect(Collectors.joining(", ")));

            sb.append(" on ");
            sb.append(screening.getMovie().getTitle());
            sb.append(" in room ");
            sb.append(screening.getRoomOfScreening().getRoomName());
            sb.append(" starting at ");
            sb.append(screening.getStartOfScreening().format(dateTimeFormatter));
            sb.append(" for ");

            sb.append(ticketList.stream()
                    .filter(x -> screening.equals(x.getScreening()))
                    .map(Ticket::getTicketPrice).reduce(Integer::sum).orElse(0));

            sb.append(" HUF");
            sb.append("\n");
        }

        return StringUtils.stripEnd(sb.toString(), "\n");

    }
}
