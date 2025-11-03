package com.shop.controller;

import com.shop.dto.ResponseDTO;
import com.shop.model.Product;
import com.shop.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseDTO<List<Product>> list() {
        return ResponseDTO.ok(productService.listAll());
    }

    @PostMapping
    public ResponseDTO<Product> create(@RequestBody Product p) {
        return ResponseDTO.ok(productService.create(p));
    }

    @PutMapping("/{id}")
    public ResponseDTO<Product> update(@PathVariable Long id, @RequestBody Product p) {
        return ResponseDTO.ok(productService.update(id, p));
    }

    @DeleteMapping("/{id}")
    public ResponseDTO<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseDTO.ok(null);
    }
}


