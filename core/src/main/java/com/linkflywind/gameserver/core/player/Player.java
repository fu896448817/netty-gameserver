package com.linkflywind.gameserver.core.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String name;
    private String nickName;
    private String sex;
    private String headPortrait;


}
