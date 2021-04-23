package com.epam.training.ticketservice.commands.account;

import com.epam.training.ticketservice.commands.Command;
import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.dao.Seat;
import com.epam.training.ticketservice.data.dao.Ticket;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.AccountDescribeInterface;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDescribeCommand implements Command {

    AccountDescribeInterface accountDescribeService;
    BookingStringBuilder bookingStringBuilder;

    public AccountDescribeCommand(AccountDescribeInterface accountDescribeService,
                                  DateTimeFormatter dateTimeFormatter) {
        this.accountDescribeService = accountDescribeService;
        this.bookingStringBuilder = new BookingStringBuilder(dateTimeFormatter);
    }

    @Override
    public String execute() {

        try {
            User user = accountDescribeService.getUser();
            //TODO LEFOGLALT JEGYEK LISTÁZÁSA
            if (User.Role.USER.equals(user.getRole())) {
                return "Signed in with account" + " '" + user.getUsername() + "'" + "\n"
                        + bookingStringBuilder.buildBookingString(user.getTickets());
            } else {
                return "Signed in with privileged account" + " '" + user.getUsername() + "'" + "\n"
                        + bookingStringBuilder.buildBookingString(user.getTickets());
            }
        } catch (UserNotLoggedInException e) {
            return e.getMessage();
        }
    }

}
class BookingStringBuilder {

    private final DateTimeFormatter dateTimeFormatter;

    public BookingStringBuilder(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public String buildBookingString(List<Ticket> ticketList) {

        if (ticketList.isEmpty()) {
            return "You have not booked any tickets yet";
        }
        List<Screening> distinctScreenings = ticketList.stream()
                .map(Ticket::getScreening).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        for (var screening : distinctScreenings) {

            sb.append("Seats ");

            sb.append(ticketList.stream()
                    .filter(x -> x.getScreening().equals(screening))
                    .map(Ticket::getSeat).map(Seat::toString)
                    .collect(Collectors.joining(", ")));

            sb.append(" ");
            sb.append("on ");
            sb.append(screening.getMovie().getTitle());
            sb.append(" in room ");
            sb.append(screening.getRoomOfScreening().getRoomName());
            sb.append("starting at ");
            sb.append(screening.getStartOfScreening().format(dateTimeFormatter));
            sb.append("for ");
            sb.append(ticketList.stream()
                    .filter(x -> screening.equals(x.getScreening()))
                    .map(Ticket::getTicketPrice).reduce(Integer::sum));
            sb.append(" HUF");
            sb.append("\n");
        }
        return sb.toString();

    }
}
