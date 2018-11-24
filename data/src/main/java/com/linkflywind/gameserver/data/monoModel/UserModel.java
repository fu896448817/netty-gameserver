package com.linkflywind.gameserver.data.monoModel;

import com.linkflywind.gameserver.data.annotation.AutoIncrement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user")
public class UserModel {

    @AutoIncrement
    private long id;
    @Indexed(unique = true)
    private String name;
    private String nickName;
    private String password;
    private String mobileNumber;
    private String sex;
    private double balance;
    private String sponsor;
    private int cardNumber;

    //0 用户 1 访客
    private int userType;
}