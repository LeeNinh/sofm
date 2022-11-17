package com.example.ninhdemo;

import com.example.ninhdemo.services.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenProvider;

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       // đọc token từ header
        String token = resolveToken(request);

        try {
            //verify token
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // có token rồi thì lấy username gọi tu db
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                // set vào context để có đăng nhập rồi
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception ex) {
            // this is very important, since it guarantees the user is not authenticated at
            // all
            SecurityContextHolder.clearContext();
            response.sendError(401, ex.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }
}