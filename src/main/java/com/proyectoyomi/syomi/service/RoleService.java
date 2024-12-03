package com.proyectoyomi.syomi.service;

import com.proyectoyomi.syomi.dao.RoleDao;
import com.proyectoyomi.syomi.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public Role createRole(Role role) {
        return roleDao.save(role);
    }
}
