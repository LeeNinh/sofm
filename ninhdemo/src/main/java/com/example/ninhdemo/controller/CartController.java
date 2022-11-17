package com.example.ninhdemo.controller;
import com.example.ninhdemo.entities.BillItem;
import com.example.ninhdemo.entities.Product;
import com.example.ninhdemo.repo.ProductRepo;
import com.example.ninhdemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    UserRepo userRepo;

    @GetMapping(value = { "/add-to-cart" })
    public String cart(@RequestParam(name = "id") int id, HttpSession session) {

        Product product = productRepo.getById(id);

        if (session.getAttribute("cart") == null) {
            BillItem billItem = new BillItem();
            billItem.setQuantity(1);
            billItem.setBuyPrice(product.getPrice());
            billItem.setProduct(product);

            Map<Integer, BillItem> map = new HashMap<Integer, BillItem>();
            map.put(id, billItem);
            // set vao session
            // hoac save db BillItemCart(userId)
            session.setAttribute("cart", map);
        } else {
            Map<Integer, BillItem> map = (Map<Integer, BillItem>) session.getAttribute("cart");
            // lay ra
            BillItem billItem = map.get(id);
            if (billItem == null) {
                billItem = new BillItem();
                billItem.setQuantity(1);
                billItem.setBuyPrice(product.getPrice());
                billItem.setProduct(product);

                map.put(id, billItem);
            } else {
                billItem.setQuantity(billItem.getQuantity() + 1);
            }
            session.setAttribute("cart", map);
        }

        return "redirect:/view-cart";
    }

    @GetMapping(value = { "/view-cart" })
    public String cart(HttpServletRequest request, HttpSession session) {
        if (session.getAttribute("cart") != null) {
            Map<Integer, BillItem> map = (Map<Integer, BillItem>) session.getAttribute("cart");

            double total = 0;
            for (Map.Entry<Integer, BillItem> entry : map.entrySet()) {
                total += (entry.getValue().getBuyPrice() * entry.getValue().getQuantity());
            }

            request.setAttribute("total", total);
        } else
            request.setAttribute("total", 0);

        return "view-cart.html";
    }

    @GetMapping(value = { "/delete-cart" })
    public String delete(HttpSession session, @RequestParam(name = "id") int id) {
        if (session.getAttribute("cart") != null) {
            Map<Integer, BillItem> map = (Map<Integer, BillItem>) session.getAttribute("cart");
            map.remove(id);
        }
        return "redirect:/view-cart";
    }

    @GetMapping(value = { "/clear-cart" })
    public String deletehome(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/view-cart";
    }

}