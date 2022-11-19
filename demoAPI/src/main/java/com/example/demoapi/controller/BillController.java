package com.example.demoapi.controller;
import com.example.demoapi.dto.ResponseDTO;
import com.example.demoapi.dto.SearchDTO;
import com.example.demoapi.dto.bill.BillDTO;
import com.example.demoapi.service.BillService;
import com.example.demoapi.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/bill")
public class BillController {

    @Autowired
    BillService billService;

    @Autowired
    MailService mailService;

    @PostMapping("/create")
    public ResponseDTO<Integer> create(@ModelAttribute BillDTO billDTO) throws MessagingException {

        int id = billService.create(billDTO);

        return ResponseDTO.<Integer>builder().code(HttpStatus.OK.value()).data(id).build();
    }

    @PutMapping("/update")
    public ResponseDTO<Void> update(@RequestBody BillDTO billDTO){
        billService.update(billDTO);
        return ResponseDTO.<Void>builder().code(HttpStatus.OK.value()).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable("id") int id){
        billService.delete(id);
        return ResponseDTO.<Void>builder().code(HttpStatus.OK.value()).build();
    }

    @GetMapping("/getOne/{id}")
    public ResponseDTO<BillDTO> getOne(@PathVariable("id") int id){
        return ResponseDTO.<BillDTO>builder().code(HttpStatus.OK.value()).data(billService.getOne(id)).build();
    }

    @PostMapping("/search")
    public ResponseDTO<List<BillDTO>> search(@RequestBody SearchDTO searchDTO){
        return  billService.search(searchDTO);
    }

    @PostMapping("/getAll")
    public ResponseDTO<List<BillDTO>> getAll(@RequestBody SearchDTO searchDTO){
        return  billService.getAll(searchDTO);
    }

}
