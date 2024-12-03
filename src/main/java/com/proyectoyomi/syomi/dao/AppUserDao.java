package com.proyectoyomi.syomi.dao;

import com.proyectoyomi.syomi.entity.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserDao extends CrudRepository<AppUser, String> {
}
