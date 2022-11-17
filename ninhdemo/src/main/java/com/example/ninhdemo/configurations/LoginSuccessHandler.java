package com.example.ninhdemo.configurations;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler  extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        UserDetails userDetails = (UserDetails)   authentication.getPrincipal();


        String redirectURL = request.getContextPath();
        for (GrantedAuthority role : userDetails.getAuthorities()) {
            if (!role.getAuthority().equals("Admin")) {
                redirectURL = "/product/search";
            } else  if (role.getAuthority().equals("SubAdmin")) {
                redirectURL = "/user/search";
            } else  if (role.getAuthority().equals("Admin")) {
                redirectURL = "/user/search";
            }
        }
        response.sendRedirect(redirectURL);

    }


}
