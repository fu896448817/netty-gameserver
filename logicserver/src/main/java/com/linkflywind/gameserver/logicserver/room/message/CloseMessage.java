package com.linkflywind.gameserver.logicserver.room.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class CloseMessage {
    private String name;
}
