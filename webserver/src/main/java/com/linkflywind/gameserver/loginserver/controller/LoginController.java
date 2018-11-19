/*
* @author   作者: qugang
* @E-mail   邮箱: qgass@163.com
* @date     创建时间：2018/11/12
* 类说明     基于WebFlex 登陆模块
*/
package com.linkflywind.gameserver.loginserver.controller;

import com.linkflywind.gameserver.core.security.JwtTokenUtil;
import com.linkflywind.gameserver.data.monoModel.UserModel;
import com.linkflywind.gameserver.data.monoRepository.UserRepository;
import com.linkflywind.gameserver.loginserver.controller.from.LoginForm;
import com.linkflywind.gameserver.loginserver.controller.from.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
public class LoginController {

    private final UserRepository userRepository;


    @Autowired
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping
    public Optional<String> login(@RequestBody LoginForm loginForm) {

        UserModel userModel =userRepository.findByNameAndPassword(loginForm.getName(), loginForm.getPassword());

        if(userModel != null)
        {
            return Optional.of(JwtTokenUtil.generateToken(userModel.getName()));
        }
        return Optional.empty();
    }

    @GetMapping
    public Optional<String> visitor(String deviceId){
        UserModel userModel = new UserModel(deviceId,
                "",
                "",
                "",
                0.0,
                "",
                "",
                "",
                1
        );
        userRepository.save(userModel);
        return Optional.of(JwtTokenUtil.generateToken(userModel.getName()));
    }

    @GetMapping
    public UserModel register(@RequestBody RegisterForm registerForm){
        //todo 验证码
        UserModel userModel = new UserModel(registerForm.getName(),
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
