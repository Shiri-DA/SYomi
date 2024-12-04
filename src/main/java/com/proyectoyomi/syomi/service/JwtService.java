package com.proyectoyomi.syomi.service;


import com.proyectoyomi.syomi.dao.AppUserDao;
import com.proyectoyomi.syomi.entity.AppUser;
import com.proyectoyomi.syomi.entity.JwtRequest;
import com.proyectoyomi.syomi.entity.JwtResponse;
import com.proyectoyomi.syomi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String username = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();
        authenticate(username, password);

        final UserDetails userDetails = loadUserByUsername(username);
        String generatedToken = jwtUtil.generateToken(userDetails);
        AppUser user = appUserDao.findById(username).get();

        JwtResponse response = new JwtResponse();
        response.setToken(generatedToken);
        response.setUsername(user.getUsername());
        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserDao.findById(username).get();
        if (user != null) {
            return new User(
                    user.getUsername(), user.getPassword(), getAuthorities(user)
            );
        } else {
            throw new UsernameNotFoundException("Username is not valid");
        }
    }

    private Set getAuthorities(AppUser user) {
        Set authorities = new HashSet();
        user.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName())));
        return authorities;
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("User is disabled");
        } catch (BadCredentialsException e) {
            throw new Exception("Bad credentials from user");
        }

    }
}
