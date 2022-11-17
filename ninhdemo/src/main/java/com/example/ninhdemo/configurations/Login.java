package com.example.ninhdemo.configurations;
import com.example.ninhdemo.entities.User;
import com.example.ninhdemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;


@Controller
public class Login {
    @Autowired
    UserRepo userRepo;
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("data");
        return "login.html";
    }
    @PostMapping("/login")
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session) throws SQLException {
        List<User> users = userRepo.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                model.addAttribute("test");
                return "redirect:/product/search";
            } else
                session.setAttribute("currentUser", user);
            return "login.html";
        }
        return "redirect:/login"; // views
    }
}
