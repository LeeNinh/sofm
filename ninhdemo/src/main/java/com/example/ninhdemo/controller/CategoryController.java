package com.example.ninhdemo.controller;

import com.example.ninhdemo.entities.Category;
import com.example.ninhdemo.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryRepo categoryRepo;


    @GetMapping("/new")
    public String create() {
        return "category/new.html";
    }
    @PostMapping("/new")
//    @ResponseStatus(HttpStatus.CREATED)
    public String create(@ModelAttribute @Valid Category department) {
        categoryRepo.save(department);
        return "redirect:/category/search";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "id", required = false) Integer id,
                              @RequestParam(name = "name", required = false) String name,

                              @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date start,
                              @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date end,

                              @RequestParam(name = "size", required = false) Integer size,
                              @RequestParam(name = "page", required = false) Integer page,
                              Model model) {

//        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setCode(200);

        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<Category> users = categoryRepo.findAllById(Arrays.asList(id));

            model.addAttribute("totalPage", 1);
            model.addAttribute("count", users.size());
            model.addAttribute("categoryList", users);
        } else {
            Page<Category> pageRS = null;

            if (StringUtils.hasText(name) && start != null && end != null) {
                pageRS = categoryRepo.searchByNameAndDate("%" + name + "%", start, end, pageable);
            } else if (StringUtils.hasText(name) && start != null) {
                pageRS = categoryRepo.searchByNameAndStartDate("%" + name + "%", start, pageable);
            } else if (StringUtils.hasText(name) && end != null) {
                pageRS = categoryRepo.searchByNameAndEndDate("%" + name + "%", end, pageable);
            } else if (StringUtils.hasText(name)) {
                pageRS = categoryRepo.searchByName("%" + name + "%", pageable);
            } else if (start != null && end != null) {
                pageRS = categoryRepo.searchByDate(start, end, pageable);
            } else if (start != null) {
                pageRS = categoryRepo.searchByStartDate(start, pageable);
            } else if (end != null) {
                pageRS = categoryRepo.searchByEndDate(end, pageable);
            } else {
                pageRS = categoryRepo.findAll(pageable);
            }

            model.addAttribute("totalPage", pageRS.getTotalPages());
            model.addAttribute("count", pageRS.getTotalElements());
            model.addAttribute("categoryList", pageRS.getContent());
        }
        // luu lai du lieu set sang view lai
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "category/category.html";
    }

    @GetMapping("/get/{id}")
    public Category get(@PathVariable("id") int id, Model model) {
        return categoryRepo.findById(id).orElse(null);
    }

    @GetMapping("/delete")
    public String delete(@PathVariable("id") int id) {
        categoryRepo.deleteById(id);
        return "redirect:/category/search";
    }
    @GetMapping("/edit") // ?id=1
    public String edit(@RequestParam("id") int id, Model model) {
        model.addAttribute("category", categoryRepo.findById(id).orElse(null));
        return "user/category.html";
    }

    @PostMapping("/edit")
    public void edit(@RequestBody @Valid Category editDeparment) {
        Category current = categoryRepo.findById(editDeparment.getId()).orElse(null);

        if (current != null) {
            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
            current.setName(editDeparment.getName());
            categoryRepo.save(current);
        }
    }
    @GetMapping("/download")
    public void download(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException {
        final String UPLOAD_FOLDER = "D:/data/";

        File file = new File(UPLOAD_FOLDER + filename);

        Files.copy(file.toPath(), response.getOutputStream());
    }
}