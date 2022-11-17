package com.example.ninhdemo.controller;

import com.example.ninhdemo.entities.Bill;
import com.example.ninhdemo.entities.BillItem;
import com.example.ninhdemo.repo.BillItemRepo;
import com.example.ninhdemo.repo.BillRepo;
import com.example.ninhdemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

@Controller
public class PaymentController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BillRepo billRepo;

    @Autowired
    private BillItemRepo billItemsRepo;


    @SuppressWarnings("unchecked")
    @GetMapping("/member/payment")
    public String payment(HttpSession session, Principal principal) {

        if (session.getAttribute("cart") != null) {
            Map<Integer, BillItem> map = (Map<Integer, BillItem>) session.getAttribute("cart");

            Bill bill = new Bill();
            bill.setUser(userRepo.findByEmail(principal.getName()));

            billRepo.save(bill);

            for (Map.Entry<Integer, BillItem> entry : map.entrySet()) {
                BillItem billItem = entry.getValue();
                billItem.setBill(bill);
                billItemsRepo.save(billItem);
            }
        }
        return "redirect:payment-success.html";
    }

    @GetMapping("/member/bills")
    public String billuser(Model model, Principal principal) {
        model.addAttribute("bills", billRepo.findByUser(principal.getName()));//name la email xem lai loginservice
        return "user/bills.html";
    }

}