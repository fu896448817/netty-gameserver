package com.linkflywind.gameserver.loginserver.redisModel;

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
    private String token;
    private String name;
}
