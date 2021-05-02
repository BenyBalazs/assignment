package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.Room;
import com.epam.training.ticketservice.data.repository.RoomRepository;
import com.epam.training.ticketservice.data.repository.SeatRepository;
import com.epam.training.ticketservice.service.user.AuthorizationService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class RoomServiceTest {

    private RoomService roomService;
    @MockBean
    private RoomRepository roomRepository;
    @MockBean
    private SeatRepository seatRepository;
    @MockBean
    private AuthorizationService authorizationService;
    private List<Room> roomList;

    @BeforeEach
    public void setUp() {
        roomList = new LinkedList<>();
        authorizationService = Mockito.mock(AuthorizationService.class);
        roomRepository = Mockito.mock(RoomRepository.class);
        seatRepository = Mockito.mock(SeatRepository.class);
        roomService = new RoomService(roomRepository, new SeatService(seatRepository), authorizationService);
        when(this.roomRepository.findById(Mockito.any(String.class)))
                .then( x -> roomList.stream().filter(y -> x.getArgument(0).equals(y.getRoomName())).findFirst());

        doAnswer(x -> roomList.add(x.getArgument(0)))
                .when(roomRepository).save(Mockito.mock(Room.class));

        doAnswer(x -> roomList.remove(x.getArgument(0)))
                .when(roomRepository).delete(Mockito.any(Room.class));

        when(this.roomRepository.findAll()).thenReturn(roomList);
    }

    @SneakyThrows
    @Test
    public void testCreateRoomShouldReturnTrueWhenMovieCreationWasSuccessful() {
        assertThat(roomService.createRoom("ballada", 10,10), equalTo(true));
    }
    @SneakyThrows
    @Test
    public void testCreateRoomShouldReturnFalseWhenTheRoomWithTheSameNameAlreadyExists() {
        roomList.add(new Room("ballada", new ArrayList<>(), null));
        assertThat(roomService.createRoom("ballada", 10,10), equalTo(false));

    }
    @SneakyThrows
    @Test
    public void testModifyRoomsShouldReturnTrueWhenTheModificationWasSuccessful() {
        roomList.add(new Room("ballada", new ArrayList<>(), new ArrayList<>()));
        assertThat(roomService.modifyRoomSeats("ballada", 33, 10), equalTo(true));
    }

    @SneakyThrows
    @Test
    public void testModifyRoomsShouldReturnFalseWhenTheRoomDoesNotExist() {
        roomList.add(new Room("ballada", new ArrayList<>(), new ArrayList<>()));
        assertThat(roomService.modifyRoomSeats("ballada2", 33, 10), equalTo(false));
    }

    @SneakyThrows
    @Test
    public void testDeleteRoomReturnTrueWhenDeleteWasSuccessful() {
        roomList.add(new Room("ballada", new ArrayList<>(), new ArrayList<>()));
        assertThat(roomService.deleteRoom("ballada"), equalTo(true));
    }

    @SneakyThrows
    @Test
    public void testDeleteRoomShouldReturnFalseWhenTryingToDeleteNonExistingRoom() {
        assertThat(roomService.deleteRoom("ballada"), equalTo(false));
    }

    @SneakyThrows
    @Test
    public void testDeleteRoomShouldRemoveTheGivenRoomFromTheList() {
        Room room = new Room("ballada", new ArrayList<>(), new ArrayList<>());
        roomList.add(room);
        roomService.deleteRoom("ballada");
        assertThat(roomList.contains(room), equalTo(false));
    }

}
