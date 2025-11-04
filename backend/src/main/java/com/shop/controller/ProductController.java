package com.shop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shop.dto.ResponseDTO;
import com.shop.model.Product;
import com.shop.service.ProductService;

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

    @GetMapping("/search")
    public ResponseDTO<List<Product>> search(@RequestParam(value = "query", required = false) String query) {
        return ResponseDTO.ok(productService.search(query));
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


