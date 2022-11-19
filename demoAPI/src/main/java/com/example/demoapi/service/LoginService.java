package com.example.demoapi.service;//package com.ducnt.project3b.service;
//
//import com.ducnt.project3b.entity.user.User;
//import com.ducnt.project3b.repo.UserRepo;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//@Service
//public class LoginService implements UserDetailsService {
//
//    @Autowired
//    UserRepo userRepo;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User user = userRepo.findByUsername(username);
//
//        if (user == null){
//            throw new UsernameNotFoundException("not found ");
//
//        }
//
//        List<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();
//
//        for (String role : user.getRoles()){
//            list.add(new SimpleGrantedAuthority(role));
//        }
//
//        //tao user cua security // user dang nhap hien tai
//
//        CurrentUser currentUser = new CurrentUser(username, user.getPassword(), list);
//        currentUser.setId(user.getId());
//        return currentUser;
//    }
//
//    @SuppressWarnings("serial")
//    @Data
//    @EqualsAndHashCode(callSuper = false)
//    static class CurrentUser extends org.springframework.security.core.userdetails.User {
//
//        private int id;
//
//        public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities){
//            super(username,password,authorities);
//        }
//
//    }
//}
