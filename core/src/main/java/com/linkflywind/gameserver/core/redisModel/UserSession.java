package com.linkflywind.gameserver.core.redisModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession implements Serializable {
    @Id
    private String name;
    private String socketId;
    private String token;
    private Long lastLoginTime;
    private Long lastLogoutTime;
    //0: 登陆 1:退出
    private String state;

}
