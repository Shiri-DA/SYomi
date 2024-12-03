package com.proyectoyomi.syomi.controller;

import com.proyectoyomi.syomi.entity.AppUser;
import com.proyectoyomi.syomi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping({"/createUser"})
    public AppUser createUser(@RequestBody AppUser appUser) {
        return userService.createUser(appUser);
    }
}
