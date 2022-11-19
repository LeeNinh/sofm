package com.example.demoapi.controller;

import com.example.demoapi.dto.ResponseDTO;
import com.example.demoapi.dto.SearchDTO;
import com.example.demoapi.dto.category.CategoryDTO;
import com.example.demoapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/create")
    public ResponseDTO<Integer> create(@ModelAttribute CategoryDTO categoryDTO, @RequestParam(name = "cateParentId") int cateParentId){

        int id = categoryService.create(categoryDTO, cateParentId);

        return ResponseDTO.<Integer>builder().code(HttpStatus.OK.value()).data(id).build();
    }

    @GetMapping("/getOne/{id}")
    public ResponseDTO<CategoryDTO> getOne(@PathVariable(name = "id") int id){
        return ResponseDTO.<CategoryDTO>builder().code(HttpStatus.OK.value()).data(categoryService.getOne(id)).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable(name = "id") int id) {
        return ResponseDTO.<Void>builder().code(HttpStatus.OK.value()).build();
    }

    @PutMapping("/update")
    public ResponseDTO<Void> update(@RequestBody @Validated CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return ResponseDTO.<Void>builder().code(HttpStatus.OK.value()).build();
    }

    @PostMapping("/search")
    public ResponseDTO<List<CategoryDTO>> search(@RequestBody SearchDTO searchDTO){

        return categoryService.search(searchDTO);
    }
}
