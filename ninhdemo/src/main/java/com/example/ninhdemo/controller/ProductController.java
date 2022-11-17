package com.example.ninhdemo.controller;

import com.example.ninhdemo.entities.BillItem;
import com.example.ninhdemo.entities.Category;
import com.example.ninhdemo.entities.Product;
import com.example.ninhdemo.repo.CategoryRepo;
import com.example.ninhdemo.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductRepo productRepo;
    @Autowired
    CategoryRepo categoryRepo;

    public  String cart(@RequestParam(name = "ad") int id,
                        HttpSession session){
        Product product = productRepo.getById(id);
        if (session.getAttribute("cart") == null){
            BillItem billItem = new BillItem();
            billItem.setQuantity(1);
            billItem.setBuyPrice(product.getPrice());
            billItem.setProduct(product);

            Map<Integer, BillItem> map = new HashMap<Integer,BillItem>();
            map.put(id,billItem);
            // set vào session
            session.setAttribute("cart", map);
        }else {
            Map<Integer, BillItem> map = new HashMap<Integer,BillItem>();
            // lấy ra check xem còn không?
            // hoặc save DB billItemCart
            BillItem billItem = map.get(id);
            if (billItem == null){
                billItem = new BillItem();
                billItem.setQuantity(1);
                billItem.setBuyPrice(product.getPrice());
                billItem.setProduct(product);

                map.put(id, billItem);

            }else {
                billItem.setQuantity(billItem.getQuantity() +1);
            }
            session.setAttribute("cart", map);
        }
        return "redirect:/";

    }

    @GetMapping("/new")
    public String create(Model model) {
        List<Category> categoryList = categoryRepo.findAll();
        model.addAttribute("categoryList", categoryList);
        return "product/new.html";
    }

    @PostMapping("/new")
//    @ResponseStatus(HttpStatus.CREATED)
    public String create(@ModelAttribute @Valid Product product,
                         @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            final String UPLOAD_FOLDER = "D:/Data/";

            String filename = file.getOriginalFilename();
            File newFile = new File(UPLOAD_FOLDER + filename);

            file.transferTo(newFile);
            product.setImage(filename);
        }
        productRepo.save(product);
        return "redirect:/product/search";
    }
    @GetMapping("/download")
    public void download(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException {
        final String UPLOAD_FOLDER = "D:/data/";

        File file = new File(UPLOAD_FOLDER + filename);

        Files.copy(file.toPath(), response.getOutputStream());
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
            List<Product> products = productRepo.findAllById(Arrays.asList(id));

            model.addAttribute("totalPage", 1);
            model.addAttribute("count", products.size());
            model.addAttribute("productList", products);
            model.addAttribute("categoryList", categoryRepo.findAll());
        } else {
            Page<Product> pageRS = null;

            if (StringUtils.hasText(name) && start != null && end != null) {
                pageRS = productRepo.searchByNameAndDate("%" + name + "%", start, end, pageable);
            } else if (StringUtils.hasText(name) && start != null) {
                pageRS = productRepo.searchByNameAndStartDate("%" + name + "%", start, pageable);
            } else if (StringUtils.hasText(name) && end != null) {
                pageRS = productRepo.searchByNameAndEndDate("%" + name + "%", end, pageable);
            } else if (StringUtils.hasText(name)) {
                pageRS = productRepo.searchByName("%" + name + "%", pageable);
            } else if (start != null && end != null) {
                pageRS = productRepo.searchByDate(start, end, pageable);
            } else if (start != null) {
                pageRS = productRepo.searchByStartDate(start, pageable);
            } else if (end != null) {
                pageRS = productRepo.searchByEndDate(end, pageable);
            } else {
                pageRS = productRepo.findAll(pageable);
            }

            model.addAttribute("totalPage", pageRS.getTotalPages());
            model.addAttribute("count", pageRS.getTotalElements());
            model.addAttribute("productList", pageRS.getContent());
            model.addAttribute("categoryList", categoryRepo.findAll());
        }
        // luu lai du lieu set sang view lai
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "product/product.html";
    }

    @GetMapping("/delete") // ?id=1
//	@Secured
    public String delete(@RequestParam("name") int id,
                         Principal principal,
                         HttpServletRequest request) {
            productRepo.deleteById(id);
        return "redirect:/product/search";
    }
    @GetMapping("/edit") // ?id=1
    public String edit(@RequestParam("id") int id, Model model) {
        List<Category> categoryList = categoryRepo.findAll();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("product", productRepo.findById(id).orElse(null));
        return "product/edit.html";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute Product editUser) throws IllegalStateException, IOException {
        Product current = productRepo.findById(editUser.getId()).orElse(null);

        if (current != null) {
            if (!editUser.getFile().isEmpty()) {
                final String UPLOAD_FOLDER = "D:/data/";

                String filename = editUser.getFile().getOriginalFilename();
                File newFile = new File(UPLOAD_FOLDER + filename);

                editUser.getFile().transferTo(newFile);

                current.setImage(filename);// save to db
            }

            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
            current.setName(editUser.getName());
            productRepo.save(current);
        }
        return "redirect:/product/search";
    }
}