package com.secondhand.service;

import com.secondhand.entity.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    
    Product getProductById(Long id);
    
    List<Product> getAllProducts();
    
    List<Product> searchProducts(String keyword);
    
    List<Product> getProductsByCategory(String category);
    
    List<Product> getProductsByPriceRange(double minPrice, double maxPrice);
    
    List<Product> getProductsBySeller(Long sellerId);
    
    Product updateProduct(Long id, Product product);
    
    void deleteProduct(Long id);
    
    Product updateProductStatus(Long id, String status);
} 