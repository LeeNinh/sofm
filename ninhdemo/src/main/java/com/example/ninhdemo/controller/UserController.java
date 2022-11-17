package com.example.ninhdemo.controller;

import com.example.ninhdemo.entities.User;
import com.example.ninhdemo.entities.UserRole;
import com.example.ninhdemo.repo.UserRepo;
import com.example.ninhdemo.repo.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserRoleRepo userRoleRepo;

    @GetMapping("/new")
    public String create() {
        return "user/new.html";
    }

    @PostMapping("/new")
//    @ResponseStatus(HttpStatus.CREATED)
    public String create(@ModelAttribute @Valid User user,
                         @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            final String UPLOAD_FOLDER = "D:/Data/";

            String filename = file.getOriginalFilename();
            File newFile = new File(UPLOAD_FOLDER + filename);

            file.transferTo(newFile);
            user.setAvatar(filename);
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        User user1 = userRepo.save(user);
        if (user1 != null){
            UserRole userRole = new UserRole();
            userRole.setUser(user1);
            userRole.setRole("Member");
            userRoleRepo.save(userRole);

        }
            return "redirect:/product/search";
    }
    @GetMapping("/download")
    public void download(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException {
        final String UPLOAD_FOLDER = "D:/data/";

        File file = new File(UPLOAD_FOLDER + filename);

        Files.copy(file.toPath(), response.getOutputStream());
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "byId", required = false) Integer id,
                         @RequestParam(name = "name", required = false) String name,

                         @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date start,
                         @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date end,

                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page, Model model) {

        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<User> users = userRepo.findAllById(Arrays.asList(id));

            model.addAttribute("totalPage", 1);
            model.addAttribute("count", users.size());
            model.addAttribute("userList", users);
        } else {
            Page<User> pageRS = null;

            if (StringUtils.hasText(name) && start != null && end != null) {
                pageRS = userRepo.searchByNameAndDate("%" + name + "%", start, end, pageable);
            } else if (StringUtils.hasText(name) && start != null) {
                pageRS = userRepo.searchByNameAndStartDate("%" + name + "%", start, pageable);
            } else if (StringUtils.hasText(name) && end != null) {
                pageRS = userRepo.searchByNameAndEndDate("%" + name + "%", end, pageable);
            } else if (StringUtils.hasText(name)) {
                pageRS = userRepo.searchByName("%" + name + "%", pageable);
            } else if (start != null && end != null) {
                pageRS = userRepo.searchByDate(start, end, pageable);
            } else if (start != null) {
                pageRS = userRepo.searchByStartDate(start, pageable);
            } else if (end != null) {
                pageRS = userRepo.searchByEndDate(end, pageable);
            } else {
                pageRS = userRepo.findAll(pageable);
            }

            model.addAttribute("totalPage", pageRS.getTotalPages());
            model.addAttribute("count", pageRS.getTotalElements());
            model.addAttribute("userList", pageRS.getContent());
        }

        // luu lai du lieu set sang view lai
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "user/user.html";
    }

//    @GetMapping("/get/{id}")
//    public User get(@PathVariable("id") int id) {
//        return userRepo.findById(id).orElse(null);
//    }

    @GetMapping("/delete") // ?id=1
//	@Secured
    public String delete(@RequestParam("id") int id,
                         Principal principal,
                         HttpServletRequest request) {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
//
//            if (auth != null
//                    && !(auth instanceof AnonymousAuthenticationToken)) {
//                /// get thong tin ra
//                //principal: chinh la user tu LoginService tra ve
//
//                if (request.isUserInRole("Admin")) {
//                    // trong DB dự liệu phải có chứ ROLE_
//                }
//            }
        //// dùng authority
        List<String> roles = auth.getAuthorities().stream().map(p -> p.getAuthority()).collect(Collectors.toList());
        if (roles.contains("Admin"))
            userRepo.deleteById(id);
        return "redirect:/user/search";
    }

    @GetMapping("/edit") // ?id=1
    public String edit(@RequestParam("id") int id, Model model) {
        model.addAttribute("user", userRepo.findById(id).orElse(null));
        return "user/edit.html";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute User editUser) throws IllegalStateException, IOException {
        User current = userRepo.findById(editUser.getId()).orElse(null);

        if (current != null) {
            if (!editUser.getFile().isEmpty()) {
                final String UPLOAD_FOLDER = "D:/data/";

                String filename = editUser.getFile().getOriginalFilename();
                File newFile = new File(UPLOAD_FOLDER + filename);

                editUser.getFile().transferTo(newFile);

                current.setAvatar(filename);// save to db
            }

            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
            current.setName(editUser.getName());
            userRepo.save(current);
        }
        return "redirect:/user/search";
    }
}