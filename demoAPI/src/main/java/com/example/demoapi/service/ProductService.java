package com.example.demoapi.service;


import com.example.demoapi.dto.ResponseDTO;
import com.example.demoapi.dto.SearchDTO;
import com.example.demoapi.dto.product.ProductDTO;
import com.example.demoapi.entities.Product;
import com.example.demoapi.repository.CategoryRepo;
import com.example.demoapi.repository.ProductRepo;
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

public interface ProductService {

    int create(ProductDTO productDTO);

    void update(ProductDTO productDTO);

    void delete(int id);

    ProductDTO getOne(int id);

    ResponseDTO<List<ProductDTO>> search(SearchDTO searchDTO);


    @Service
    @Transactional
    class ProductServiceImpl implements ProductService {

        @Autowired
        ProductRepo productRepo;

        @Autowired
        CategoryRepo categoryRepo;

        @Override
        public int create(ProductDTO productDTO) {
            Product product = new ModelMapper().createTypeMap(ProductDTO.class, Product.class).map(productDTO);
//            Category category = categoryRepo.findById(cateId).orElseThrow(NoResultException::new);
//            product.setCategory(category);
            productRepo.save(product);
            return product.getId();
        }

        @Override
        public void update(ProductDTO productDTO) {
            Product product = productRepo.findById(productDTO.getId()).orElseThrow(NoResultException::new);
            Product newProduct = new ModelMapper().createTypeMap(ProductDTO.class, Product.class).map(productDTO);

            if (productDTO.getUrlImage() == null) {
                newProduct.setUrlImage(product.getUrlImage());
            }

            productRepo.save(newProduct);
        }

        @Override
        public void delete(int id) {
            productRepo.deleteById(id);
        }

        @Override
        public ProductDTO getOne(int id) {
            Product product = productRepo.findById(id).orElseThrow(NoResultException::new);
            ProductDTO productDTO = new ModelMapper().createTypeMap(product, ProductDTO.class).map(product);

            return productDTO;
        }

        @Override
        public ResponseDTO<List<ProductDTO>> search(SearchDTO searchDTO) {
            Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize());

            Page<Product> pageProduct = productRepo.searchByName("%" + searchDTO.getKeyword() + "%", pageable);

            ResponseDTO<List<ProductDTO>> resp = new ResponseDTO<List<ProductDTO>>();
            resp.setCode(HttpStatus.OK.value());
            resp.setTotalPage(pageProduct.getTotalPages());

            List<ProductDTO> productDTOS = new ArrayList<ProductDTO>();

            for (Product product : pageProduct.getContent()) {
                ProductDTO productDTO = new ModelMapper().map(product, ProductDTO.class);
                productDTOS.add(productDTO);
            }

            resp.setData(productDTOS);
            return resp;
        }
    }
}
