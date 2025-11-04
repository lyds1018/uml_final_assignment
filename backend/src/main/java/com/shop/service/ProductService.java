package com.shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.model.Product;
import com.shop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listAll() { return productRepository.findAll(); }
    public Product create(Product p) { return productRepository.save(p); }
    public Product update(Long id, Product p) {
        p.setId(id);
        return productRepository.save(p);
    }
    public void delete(Long id) { productRepository.deleteById(id); }

    public List<Product> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return listAll();
        }
        return productRepository.findByNameContainingIgnoreCase(query.trim());
    }
}


