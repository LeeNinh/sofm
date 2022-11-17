package com.example.ninhdemo.controller;

import com.example.ninhdemo.dto.ResponseDTO;
import com.example.ninhdemo.entities.Bill;
import com.example.ninhdemo.repo.BillItemRepo;

import com.example.ninhdemo.repo.BillRepo;
import com.example.ninhdemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {
    @Autowired
    BillRepo billRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    BillItemRepo billItemRepo;


    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Bill add(@ModelAttribute @Valid Bill bill) throws IOException {
        billRepo.save(bill);
        return bill;
    }

    @GetMapping("/search")
    public ResponseDTO search(@RequestParam(name = "id", required = false) Integer id,
                              @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date start,
                              @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date end,
                              @RequestParam(name = "size", required = false) Integer size,
                              @RequestParam(name = "page", required = false) Integer page) {

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(200);


        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<Bill> bills = billRepo.findAllById(Arrays.asList(id));

            responseDTO.setTotalPage(1);
            responseDTO.setCount((long) bills.size());
            responseDTO.setData(bills);
        } else {
            Page<Bill> pageRS = null;

            if (start != null && end != null) {
                pageRS = billRepo.searchByDate(start, end, pageable);
            } else if (start != null) {
                pageRS = billRepo.searchByStartDate(start, pageable);
            } else if (end != null) {
                pageRS = billRepo.searchByEndDate(end, pageable);
            } else {
                pageRS = billRepo.findAll(pageable);
                billRepo.thongKeBill();
                billRepo.thongKeBill2();
            }
            responseDTO.setTotalPage(pageRS.getTotalPages());
            responseDTO.setCount(pageRS.getTotalElements());
            responseDTO.setData(pageRS.getContent());

        }
        return responseDTO;
    }

    @GetMapping("/get/{id}")
    public Bill get(@PathVariable("id") int id) {
        return billRepo.findById(id).orElse(null);
    }

    @DeleteMapping("/delete/{id}") // /1
    public void delete(
            @PathVariable("id") int id,
            @RequestParam(name = "del", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date del) {
        billRepo.deleteById(id);
        billRepo.deleteByCreateaAt(del);
    }

    @PutMapping("/edit")
    public void edit(
            @RequestBody
            @Valid Bill editBill) {
        Bill current = billRepo.findById(editBill.getId()).orElse(null);
        billRepo.save(current);
    }
}