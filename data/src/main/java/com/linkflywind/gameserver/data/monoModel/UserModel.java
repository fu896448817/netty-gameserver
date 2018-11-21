package com.linkflywind.gameserver.data.monoModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class UserModel {

    @Id
    private String name;
    private String password;
    private String mobileNumber;
    private String sex;
    private double balance;
    private String sponsor;
    private String eosAddress;
    private String eosPrivateKey;
    private int cardNumber;
    private String token;
}