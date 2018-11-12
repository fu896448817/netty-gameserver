package com.linkflywind.gameserver.loginserver.monoRepository;

import com.linkflywind.gameserver.loginserver.monoModel.UserModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<UserModel,String> {
    Mono<UserModel> findByNameAndPassword(String name, String password);
}
