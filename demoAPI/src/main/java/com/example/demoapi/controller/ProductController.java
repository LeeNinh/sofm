package com.example.demoapi.controller;

import com.example.demoapi.dto.ResponseDTO;
import com.example.demoapi.dto.SearchDTO;
import com.example.demoapi.dto.product.ProductDTO;
import com.example.demoapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/admin/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/create")
    public ResponseDTO<Integer> create(@ModelAttribute ProductDTO productDTO){

        if (productDTO.getFile() != null && productDTO.getFile().getSize() > 0){
            final String FOLDER = "E:\\HinhAnhSanPham\\";

            String filename = productDTO.getFile().getOriginalFilename();

            File outputFile = new File(FOLDER + filename);

            try {
                productDTO.getFile().transferTo(outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            productDTO.setUrlImage("/api/product/download?filename=" + filename);
        }

        int id =productService.create(productDTO);

        return ResponseDTO.<Integer>builder().code(HttpStatus.OK.value()).data(id).build();
    }

    @GetMapping("/download")
    public void download(@RequestParam( name = "filename") String filename, HttpServletResponse response) throws IOException {
        final String FOLDER = "E:\\HinhAnhSanPham\\";

        File file = new File(FOLDER + filename);
        if (file.exists()){
            response.setHeader("Content-Disposition", "inline; filename=\"" +filename +"\"");
            response.setContentType("application/octet-stream; name=\"" +filename + "\"");

            Files.copy(file.toPath(), response.getOutputStream());
        }
    }

    @GetMapping("/getOne/{id}")
    public ResponseDTO<ProductDTO> getOne(@PathVariable("id") int id){
        return ResponseDTO.<ProductDTO>builder().code(HttpStatus.OK.value()).data(productService.getOne(id)).build();
    }

    @GetMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable("id") int id){
         productService.delete(id);

         return ResponseDTO.<Void>builder().code(HttpStatus.OK.value()).build();
    }

    @PostMapping("/search")
    public ResponseDTO<List<ProductDTO>> search(@RequestBody SearchDTO searchDTO){
        return productService.search(searchDTO);
    }

    @PutMapping("/update")
    public ResponseDTO<Void> update(@ModelAttribute(name = "product") @Validated ProductDTO productDTO){

        if (productDTO.getFile() != null && productDTO.getFile().getSize() > 0){
            final String FOLDER = "E:\\HinhAnhSanPham\\";

            String filename = productDTO.getFile().getOriginalFilename();

            File outputFile = new File(FOLDER + filename);

            try {
                productDTO.getFile().transferTo(outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            productDTO.setUrlImage("/api/product/download?filename=" + filename);
        }
        productService.update(productDTO);
        return ResponseDTO.<Void>builder().code(HttpStatus.OK.value()).build();
    }

}
