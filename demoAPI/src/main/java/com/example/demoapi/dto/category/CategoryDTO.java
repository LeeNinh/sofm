package com.example.demoapi.dto.category;


import com.example.demoapi.entities.Category;
import lombok.Data;

@Data
public class CategoryDTO {
	
	private int id;
	
	private String name;

	private Category parentCategory;
	
}
