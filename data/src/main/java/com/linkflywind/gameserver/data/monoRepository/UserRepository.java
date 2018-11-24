package com.linkflywind.gameserver.data.monoRepository;

import com.linkflywind.gameserver.data.monoModel.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserModel,String> {

    Optional<UserModel> findById(long id);
    UserModel findByNameAndPassword(String name, String password);
    UserModel findByName(String name);
    UserModel findByNameAndUserType(String name,int userType);
}