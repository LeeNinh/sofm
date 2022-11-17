package com.example.ninhdemo.configurations;

import com.example.ninhdemo.services.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class LoginAPI {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenService jwtTokenProvider;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Principal me(Principal principal){return principal;}

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return jwtTokenProvider.createToken(username);
    }
}