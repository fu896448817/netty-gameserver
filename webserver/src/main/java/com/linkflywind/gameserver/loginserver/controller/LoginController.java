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

    private final JwtTokenUtil jwtTokenUtil;


    @Autowired
    public LoginController(UserRepository userRepository,
                           GameConfig config,
                           RedisTemplate redisTemplate,
                           JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.config = config;
        this.redisTemplate = redisTemplate;
        this.jwtTokenUtil = jwtTokenUtil;
        this.valueOperationsByGameWebSocketSession = this.redisTemplate.opsForValue();
    }


    @PostMapping("api/login")
    public Optional<LoginResponse> login(@RequestBody LoginRequest loginForm) {
        UserModel userModel = userRepository.findByNameAndPassword(loginForm.getName(), loginForm.getPassword());
        if (userModel != null) {
            String token = jwtTokenUtil.generateToken(userModel.getName());
            LoginResponse loginResponse = new LoginResponse(userModel.getName(),
                    userModel.getNickName(),
                    token,
                    0,
                    0,
                    config.getServers()
            );

            return Optional.of(loginResponse);
        }
        return Optional.empty();
    }

    @GetMapping("api/guest")
    public GuestResponse guest(String deviceId) {
        String token = jwtTokenUtil.generateToken(deviceId);
        UserModel userModel = userRepository.findByNameAndUserType(deviceId, 1);


        if (userModel == null) {
            userModel = new UserModel();
            userModel.setName(deviceId);
            userModel.setNickName("");
            userModel.setBalance(0);
            userModel.setCardNumber(3);
            userModel.setMobileNumber("");
            userModel.setPassword("");
            userModel.setSex("0");
            userModel.setSponsor("");
            userModel.setUserType(1);
            userRepository.save(userModel);
        }

        return new GuestResponse(userModel.getId()
                ,userModel.getName(),
                userModel.getNickName(),
                token,
                0,
                3,
                config.getServers()
        );
    }

    @PostMapping("api/register")
    public UserModel register(@RequestBody RegisterRequest registerForm) {
        //todo 验证码
        UserModel userModel = new UserModel();

        userModel.setName( registerForm.getName());
        userModel.setNickName( registerForm.getNickName());
        userModel.setBalance(0);
        userModel.setCardNumber(3);
        userModel.setMobileNumber( registerForm.getMobileNumber());
        userModel.setPassword( registerForm.getPassword());
        userModel.setSex("0");
        userModel.setSponsor("");
        userModel.setUserType(0);
        userRepository.save(userModel);
        return userModel;
    }
}
