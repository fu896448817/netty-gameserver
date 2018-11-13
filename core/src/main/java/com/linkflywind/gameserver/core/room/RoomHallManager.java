package com.linkflywind.gameserver.core.room;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoomHallManager {
    private List<RoomHall> roomHallList = new ArrayList<>();

    public List<RoomHall> getRoomHallList() {
        return roomHallList;
    }

    public void setRoomHallList(List<RoomHall> roomHallList) {
        this.roomHallList = roomHallList;
    }



}
