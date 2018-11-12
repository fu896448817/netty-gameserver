/*
* @author   作者: qugang
* @E-mail   邮箱: qgass@163.com
* @date     创建时间：2018/11/12
* 类说明     基于WebFlex 登陆模块
*/
package com.linkflywind.gameserver.loginserver.controller;

import com.linkflywind.gameserver.loginserver.controller.from.LoginForm;
import com.linkflywind.gameserver.loginserver.controller.from.RegisterForm;
import com.linkflywind.gameserver.loginserver.monoModel.UserModel;
import com.linkflywind.gameserver.loginserver.monoRepository.UserRepository;
import com.linkflywind.gameserver.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
public class LoginController {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public LoginController(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @GetMapping
    public Mono<Optional<String>> login(@RequestBody LoginForm loginForm) {
        return userRepository.findByNameAndPassword(loginForm.getName(), loginForm.getPassword()).map(p->
                Optional.of( JwtTokenUtil.generateToken(p.getName())));
    }

    @GetMapping
    public Mono<Boolean> register(@RequestBody RegisterForm registerForm){
        //todo 验证码
        UserModel userModel = new UserModel(registerForm.getName(),
                registerForm.getPassword(),
                registerForm.getMobileNumber(),
                registerForm.getSex(),
                0.0,
                registerForm.getSponsor(),
                "",
                ""
                );
        return userRepository.save(userModel).map(userModel1 -> true);
    }

    @GetMapping
    public Mono<Boolean> findByName(String name){
        return userRepository.findById(name).map(userModel -> true);
    }

}
