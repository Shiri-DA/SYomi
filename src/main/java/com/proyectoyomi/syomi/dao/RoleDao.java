package com.proyectoyomi.syomi.dao;

import com.proyectoyomi.syomi.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends CrudRepository<Role, String> {
}
