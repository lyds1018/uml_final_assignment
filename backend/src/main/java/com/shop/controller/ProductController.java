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

@RestController                         // 标记为 REST 控制器，返回 JSON
@RequestMapping("/api/products")        // 所有接口以 /api/products 开头
public class ProductController {
    private final ProductService productService;   // 注入业务逻辑层

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 查询全部产品
    @GetMapping
    public ResponseDTO<List<Product>> list() {
        // 调用 service 获取所有产品，包装成统一响应
        return ResponseDTO.ok(productService.listAll());
    }

    // 搜索产品，可根据关键字查询
    @GetMapping("/search")
    public ResponseDTO<List<Product>> search(
            @RequestParam(value = "query", required = false) String query) {
        // query 可为空，由 service 决定如何处理
        return ResponseDTO.ok(productService.search(query));
    }

    // 创建新产品
    @PostMapping
    public ResponseDTO<Product> create(@RequestBody Product p) {
        // Body 里的 JSON 映射为 Product 对象
        return ResponseDTO.ok(productService.create(p));
    }

    // 更新已有产品
    @PutMapping("/{id}")
    public ResponseDTO<Product> update(@PathVariable Long id, @RequestBody Product p) {
        // 用路径参数指定更新哪一个产品
        return ResponseDTO.ok(productService.update(id, p));
    }

    // 删除产品
    @DeleteMapping("/{id}")
    public ResponseDTO<Void> delete(@PathVariable Long id) {
        productService.delete(id);   // 调用 service 删除
        return ResponseDTO.ok(null); // 返回空数据的成功响应
    }
}



