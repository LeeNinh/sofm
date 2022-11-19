package com.example.demoapi.dto.billitems;

import com.example.demoapi.entities.Bill;
import com.example.demoapi.entities.Product;
import lombok.Data;

@Data
public class BillItemsDTO {
	
	private int id;
	
    Bill bill;
	
    Product product;
	
	private int quantity;
	
	private double buyPrice;
	
	
}
