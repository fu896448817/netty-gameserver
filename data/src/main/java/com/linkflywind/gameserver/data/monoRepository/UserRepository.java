package com.linkflywind.gameserver.data.monoRepository;

import com.linkflywind.gameserver.data.monoModel.UserModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<UserModel,String> {
    Mono<UserModel> findByNameAndPassword(String name, String password);
}