package com.example.ninhdemo.controller;

import com.example.ninhdemo.dto.ResponseDTO;
import com.example.ninhdemo.entities.UserRole;
import com.example.ninhdemo.repo.UserRepo;
import com.example.ninhdemo.repo.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/userrole")
public class UserRoleController {
    @Autowired
    UserRoleRepo userRoleJPARepo;
    @Autowired
    UserRepo userJPARepo;

    @GetMapping("/search")
    public ResponseDTO search(Model model,
                              @RequestParam(name = "id", required = false) Integer id,
                              @RequestParam(name = "role", required = false, defaultValue = "") String role,
                              @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "size", required = false) Integer size) {

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(200);

        page = page == null ? 0 : page;
        size = (size == null ? 5 : size);
        Pageable pageable = PageRequest.of(page, size);
        if (id != null) {
            List<UserRole> users = userRoleJPARepo.findAllById(Arrays.asList(id));

            responseDTO.setTotalPage(1);
            responseDTO.setCount((long) users.size());
            responseDTO.setData(users);
        } else {
            Page<UserRole> pageRS = null;
            if (StringUtils.hasText(role)) {
                pageRS = userRoleJPARepo.searchByRole("%" + role + "%", pageable);
            }else {
                pageRS = userRoleJPARepo.findAll(pageable);
            }
            responseDTO.setTotalPage(pageRS.getTotalPages());
            responseDTO.setCount(pageRS.getTotalElements());
            responseDTO.setData(pageRS.getContent());
        }
        return responseDTO;
    }

    @PostMapping(value = "/new")
    public UserRole create(@ModelAttribute("userrole") @Valid UserRole userRole,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return userRole;
        }
        userRoleJPARepo.save(userRole);
        return userRole;
    }

    @DeleteMapping("/delete/{id}") // /1
//    @Secured({"Addmin"})
    public void delete(
            @PathVariable("id") int id,
            Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            //get ra thông tin
            //principal: chinh la user tu LoginService tra ve
        }

        userRoleJPARepo.deleteById(id);
    }

    @GetMapping("/get/{id}")
    public UserRole get(@PathVariable("id") int id) {
        return userRoleJPARepo.findById(id).orElse(null);
    }

    @PutMapping("/edit")
    public void edit(
            @RequestBody
            @Valid UserRole editUser) {
        UserRole current = userRoleJPARepo.findById(editUser.getId()).orElse(null);

        if (current != null) {
            current.setRole(editUser.getRole());
            userRoleJPARepo.save(current);
        }
    }
}