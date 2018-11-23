/*
 * @author   作者: qugang
 * @E-mail   邮箱: qgass@163.com
 * @date     创建时间：2018/11/12
 * 类说明     基于WebFlex 登陆模块
 */
package com.linkflywind.gameserver.loginserver.controller;

import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.security.JwtTokenUtil;
import com.linkflywind.gameserver.data.monoModel.UserModel;
import com.linkflywind.gameserver.data.monoRepository.UserRepository;
import com.linkflywind.gameserver.loginserver.controller.config.GameConfig;
import com.linkflywind.gameserver.loginserver.controller.protocol.GuestResponse;
import com.linkflywind.gameserver.loginserver.controller.protocol.LoginRequest;
import com.linkflywind.gameserver.loginserver.controller.protocol.LoginResponse;
import com.linkflywind.gameserver.loginserver.controller.protocol.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LoginController {

    private final UserRepository userRepository;

    private final GameConfig config;

    protected final ValueOperations<String, GameWebSocketSession> valueOperationsByGameWebSocketSession;


    private final RedisTemplate redisTemplate;


    @Autowired
    public LoginController(UserRepository userRepository, GameConfig config, RedisTemplate redisTemplate) {

        this.userRepository = userRepository;
        this.config = config;
        this.redisTemplate = redisTemplate;

        this.valueOperationsByGameWebSocketSession = this.redisTemplate.opsForValue();
    }


    @PostMapping("api/login")
    public Optional<LoginResponse> login(@RequestBody LoginRequest loginForm) {

        UserModel userModel = userRepository.findByNameAndPassword(loginForm.getName(), loginForm.getPassword());

        if (userModel != null) {
            String token = JwtTokenUtil.generateToken(userModel.getName());
            LoginResponse loginResponse = new LoginResponse(userModel.getName(),
                    userModel.getNickName(),
                    token,
                    0,
                    0,
                    config.getGameServerLists()
            );

            return Optional.of(loginResponse);
        }
        return Optional.empty();
    }

    @GetMapping("api/guest")
    public GuestResponse guest(String deviceId) {

        String token = JwtTokenUtil.generateToken(deviceId);
        UserModel userModel = new UserModel(deviceId,
                "访客",
                "",
                "",
                "",
                0.0,
                "",
                "",
                "",
                3

        );
        userRepository.save(userModel);
        return new GuestResponse(userModel.getName(),
                userModel.getNickName(),
                token,
                0,
                3,
                config.getGameServerLists()
        );
    }

    @PostMapping("api/register")
    public UserModel register(@RequestBody RegisterRequest registerForm) {
        //todo 验证码
        UserModel userModel = new UserModel(registerForm.getName(),
                registerForm.getNickName(),
                registerForm.getPassword(),
                registerForm.getMobileNumber(),
                registerForm.getSex(),
                0.0,
                registerForm.getSponsor(),
                "",
                "",
                3
        );
        userRepository.save(userModel);
        return userModel;
    }
}
