package com.example.demoapi.service;

import com.example.demoapi.dto.ResponseDTO;
import com.example.demoapi.dto.SearchDTO;
import com.example.demoapi.dto.category.CategoryDTO;
import com.example.demoapi.entities.Category;
import com.example.demoapi.repository.CategoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public interface CategoryService {

    int create(CategoryDTO categoryDTO, int cateId);


    void update(CategoryDTO categoryDTO);

    void delete(int id);

    CategoryDTO getOne(int id);

    ResponseDTO<List<CategoryDTO>> search(SearchDTO searchDTO);


    @Service
    @Transactional
    class CategoryServiceImpl implements CategoryService {

        @Autowired
        CategoryRepo categoryRepo;

        @Override
        public int create(CategoryDTO categoryDTO, int cateParentId) {


            Category category = new ModelMapper().createTypeMap(CategoryDTO.class, Category.class).map(categoryDTO);
            Category categoryParent = categoryRepo.findById(cateParentId).orElseThrow(NoResultException::new);

            category.setParentCategory(categoryParent);
            categoryRepo.save(category);
            return category.getId();
        }

        @Override
        public void update(CategoryDTO categoryDTO) {
            Category newCategory = new ModelMapper().createTypeMap(CategoryDTO.class, Category.class).map(categoryDTO);
            categoryRepo.save(newCategory);
        }

        @Override
        public void delete(int id) {
            categoryRepo.deleteById(id);
        }

        @Override
        public CategoryDTO getOne(int id) {
            Category category = categoryRepo.findById(id).orElseThrow(NoResultException::new);
            CategoryDTO categoryDTO = new ModelMapper().map(category, CategoryDTO.class);

            return categoryDTO;
        }


        @Override
        public ResponseDTO<List<CategoryDTO>> search(SearchDTO searchDTO) {

            Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize());

            Page<Category> pageCategory = categoryRepo.searchByName("%" + searchDTO.getKeyword() + "%", pageable);

            ResponseDTO<List<CategoryDTO>> resp = new ResponseDTO<List<CategoryDTO>>();

            resp.setCode(HttpStatus.OK.value());
            resp.setTotalPage(pageCategory.getTotalPages());

            List<CategoryDTO> categoryDTOS = new ArrayList<CategoryDTO>();
            for (Category category : pageCategory.getContent()) {

                CategoryDTO categoryDTO = new ModelMapper().map(category, CategoryDTO.class);

                categoryDTOS.add(categoryDTO);
            }
            resp.setData(categoryDTOS);

            return resp;
        }
    }
}
