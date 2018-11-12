/*
* @author   作者: qugang
* @E-mail   邮箱: qgass@163.com
* @date     创建时间：2018/11/12
* 类说明     基于WebFlex 登陆模块
*/
package com.linkflywind.gameserver.loginserver.controller;

import com.linkflywind.gameserver.loginserver.controller.from.LoginForm;
import com.linkflywind.gameserver.loginserver.controller.from.LoginOutForm;
import com.linkflywind.gameserver.loginserver.controller.from.RegisterForm;
import com.linkflywind.gameserver.loginserver.monoModel.UserModel;
import com.linkflywind.gameserver.loginserver.monoRepository.UserRepository;
import com.linkflywind.gameserver.loginserver.redisModel.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@RestController
public class LoginController {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ReactiveRedisOperations<String, UserSession> userSessionOps;


    @GetMapping
    public Mono<Optional<String>> login(@RequestBody LoginForm loginForm) {
        String token = UUID.randomUUID().toString();
        return userRepository.findByNameAndPassword(loginForm.getName(), loginForm.getPassword())
                .flatMap(p -> userSessionOps.opsForValue().set(UUID.randomUUID().toString(),
                        new UserSession(UUID.randomUUID().toString(),
                                loginForm.getName())).map(result -> {
                    if (result) {
                        return Optional.of(token);
                    } else {
                        return Optional.empty();
                    }
                }));
    }

    @GetMapping
    public Mono<Boolean> logout(@RequestBody LoginOutForm loginOutForm) {
        return userSessionOps.opsForValue().delete(loginOutForm.getToken());
    }

    @GetMapping
    public Mono<Boolean> Verification(String token) {
        return userSessionOps.hasKey(token);
    }

    @GetMapping
    public Mono<Boolean> register(@RequestBody RegisterForm registerForm){
        //todo 验证码
        UserModel userModel = new UserModel(registerForm.getName(),
                registerForm.getPassword(),
                registerForm.getMobileNumber(),
                registerForm.getSex(),
                0.0,
                registerForm.getSponsor()
                );
        return userRepository.save(userModel).map(userModel1 -> true);
    }

    @GetMapping
    public Mono<Boolean> findByName(String name){
        return userRepository.findById(name).map(userModel -> true);
    }

}
