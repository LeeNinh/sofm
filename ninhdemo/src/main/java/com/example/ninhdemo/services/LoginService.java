package com.example.ninhdemo.services;

import com.example.ninhdemo.entities.User;
import com.example.ninhdemo.entities.UserRole;
import com.example.ninhdemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User st = userRepo.findByUsername(username);

        if (st == null) {
            throw new UsernameNotFoundException("not found");
        }

        List<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();

        for (UserRole role : st.getUserRoles()) {
            list.add(new SimpleGrantedAuthority(role.getRole()));
        }

        // tao user cua security
        // user dang nhap hiện tại
        org.springframework.security.core.userdetails.User currentUser = new org.springframework.security.core.userdetails.User(
                username, st.getPassword(), list);
        return currentUser;
    }
}