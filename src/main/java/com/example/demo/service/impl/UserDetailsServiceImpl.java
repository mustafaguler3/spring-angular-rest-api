package com.example.demo.service.impl;

import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = accountService.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Username not found "+ username);
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Set<UserRole> userRoles = user.getUserRoles();

        userRoles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(userRoles.toString()));
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

}


















