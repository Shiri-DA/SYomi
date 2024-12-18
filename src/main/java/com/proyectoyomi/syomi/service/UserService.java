package com.proyectoyomi.syomi.service;

import com.proyectoyomi.syomi.dao.AppUserDao;
import com.proyectoyomi.syomi.dao.RoleDao;
import com.proyectoyomi.syomi.entity.AppUser;
import com.proyectoyomi.syomi.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private RoleDao roleDao;

    public AppUser createUser(AppUser user) {
        // Find User role
        Role role = roleDao.findById("USER").get();

        // Asign user role to creation of new user
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        // Encrypt password
        user.setPassword(getEncryptedPassword(user.getPassword()));

        // Save and return user
        return appUserDao.save(user);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    // Just for config TODO: delete before production
    public void initRolesAndUsers() {
        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        adminRole.setRoleDescription("Administrador");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("USER");
        userRole.setRoleDescription("Usuario");
        roleDao.save(userRole);

        AppUser adminUser = new AppUser();
        adminUser.setUsername("admin");
        adminUser.setPassword(getEncryptedPassword("admin"));
        Set<Role> adminRoles = new HashSet<Role>();
        adminRoles.add(adminRole);
        adminUser.setRoles(adminRoles);
        appUserDao.save(adminUser);

        AppUser user1 = new AppUser();
        user1.setUsername("user1");
        user1.setPassword(getEncryptedPassword("user1"));
        Set<Role> userRoles = new HashSet<Role>();
        userRoles.add(userRole);
        user1.setRoles(userRoles);
        appUserDao.save(user1);
    }

    public String getEncryptedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
