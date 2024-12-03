package com.proyectoyomi.syomi.controller;

import com.proyectoyomi.syomi.entity.AppUser;
import com.proyectoyomi.syomi.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // Just for config TODO: delete before production
    @PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesAndUsers();
    }

    @PostMapping({"/createUser"})
    public AppUser createUser(@RequestBody AppUser appUser) {
        return userService.createUser(appUser);
    }

}
