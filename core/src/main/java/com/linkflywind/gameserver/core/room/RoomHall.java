package com.linkflywind.gameserver.core.room;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class RoomHall {
    private String name;
    private int littleChip;
    private int intoChip;
    List<Room> list = new ArrayList<>();
}
