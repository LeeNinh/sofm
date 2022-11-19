package com.example.demoapi.service;


import com.example.demoapi.dto.MessageDTO;
import com.example.demoapi.dto.ResponseDTO;
import com.example.demoapi.dto.SearchDTO;
import com.example.demoapi.dto.bill.BillDTO;
import com.example.demoapi.entities.Bill;
import com.example.demoapi.entities.User;
import com.example.demoapi.repository.BillRepo;
import com.example.demoapi.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface BillService {

    int create(BillDTO billDTO) throws MessagingException;

    void update(BillDTO billDTO);

    void delete(int id);

    BillDTO getOne(int id);

    ResponseDTO<List<BillDTO>> search(SearchDTO searchDTO);

    ResponseDTO<List<BillDTO>> getAll(SearchDTO searchDTO);

    @Service
    @Transactional
    class BillServiceImpl implements BillService {

        @Autowired
        BillRepo billRepo;

        @Autowired
        UserRepo userRepo;

        @Autowired
        MailService mailService;
        @Override
        public int create(BillDTO billDTO) throws MessagingException {

            Bill bill = new ModelMapper().createTypeMap(BillDTO.class, Bill.class)
                    .addMappings(map -> map.skip(Bill :: setBuyDate)).map(billDTO);

            bill.setBuyDate(new Date());
            billRepo.save(bill);

            //send email
            User user = userRepo.findById(bill.getUser().getId()).orElse(null);
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setToName(user.getName());
            messageDTO.setTo(user.getEmail());
            mailService.sendBillEmail(messageDTO);

            return bill.getId();
        }

        @Override
        public void update(BillDTO billDTO) {

            Bill bill = billRepo.findById(billDTO.getId()).orElseThrow(NoResultException::new);
            Bill newBill = new ModelMapper().createTypeMap(BillDTO.class, Bill.class)
                    .addMappings(map -> map.skip(Bill :: setBuyDate)).map(billDTO);

            newBill.setBuyDate(bill.getBuyDate());
            billRepo.save(newBill);
        }

        @Override
        public void delete(int id) {
            billRepo.deleteById(id);
        }

        @Override
        public BillDTO getOne(int id) {
            Bill bill = billRepo.findById(id).orElseThrow(NoResultException::new);
            BillDTO billDTO = new ModelMapper().map(bill, BillDTO.class);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            billDTO.setBuyDate(sdf.format(bill.getBuyDate()));
            return billDTO;
        }

        @Override
        public ResponseDTO<List<BillDTO>> search(SearchDTO searchDTO) {
            Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize());
            Page<Bill> pageBill = billRepo.searchByUserId(Integer.parseInt(searchDTO.getKeyword()), pageable);

            ResponseDTO<List<BillDTO>> resp = new ResponseDTO<List<BillDTO>>();
            resp.setCode(HttpStatus.OK.value());
            resp.setTotalPage(pageBill.getTotalPages());

            List<BillDTO> billDTOS = new ArrayList<BillDTO>();
            for (Bill bill : pageBill.getContent()){
                BillDTO billDTO = new ModelMapper().map(bill, BillDTO.class);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                billDTO.setBuyDate(sdf.format(bill.getBuyDate()));

                billDTOS.add(billDTO);
            }

            resp.setData(billDTOS);
            return resp;
        }

        @Override
        public ResponseDTO<List<BillDTO>> getAll(SearchDTO searchDTO) {
            Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize());
            Page<Bill> pageBill = billRepo.getAll(pageable);

            ResponseDTO<List<BillDTO>> resp = new ResponseDTO<List<BillDTO>>();
            resp.setCode(HttpStatus.OK.value());
            resp.setTotalPage(pageBill.getTotalPages());

            List<BillDTO> billDTOS = new ArrayList<BillDTO>();
            for (Bill bill : pageBill.getContent()){
                BillDTO billDTO = new ModelMapper().map(bill, BillDTO.class);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                billDTO.setBuyDate(sdf.format(bill.getBuyDate()));

                billDTOS.add(billDTO);
            }

            resp.setData(billDTOS);
            return resp;
        }

    }
}
