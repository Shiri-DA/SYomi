package com.proyectoyomi.syomi.service;

import com.proyectoyomi.syomi.dao.AppUserDao;
import com.proyectoyomi.syomi.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private AppUserDao appUserDao;

    public AppUser createUser(AppUser user) {
        return appUserDao.save(user);
    }
}
