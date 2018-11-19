package com.linkflywind.gameserver.data.monoRepository;

import com.linkflywind.gameserver.data.monoModel.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserModel,String> {
    UserModel findByNameAndPassword(String name, String password);
    UserModel findByName(String name);
}