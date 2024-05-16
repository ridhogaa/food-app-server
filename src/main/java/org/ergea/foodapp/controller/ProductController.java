package org.ergea.foodapp.controller;

import org.ergea.foodapp.dto.BaseResponse;
import org.ergea.foodapp.dto.ProductRequest;
import org.ergea.foodapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(BaseResponse.success(productService.create(request), "Success Create Product"));
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price
    ) {
        return ResponseEntity.ok(BaseResponse.success(productService.findAll(pageable, name, price), "Success Get All Products"));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(BaseResponse.success(productService.update(id, request), "Success Update Product"));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(BaseResponse.success(productService.delete(id), "Success Delete Product"));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(BaseResponse.success(productService.findById(id), "Success Get Detail Product"));
    }
}
