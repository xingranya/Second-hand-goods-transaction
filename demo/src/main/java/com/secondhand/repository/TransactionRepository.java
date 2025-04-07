package com.secondhand.repository;

import com.secondhand.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBuyerId(Long buyerId);
    
    List<Transaction> findBySellerId(Long sellerId);
    
    List<Transaction> findByProductId(Long productId);
    
    List<Transaction> findByStatus(String status);
    
    List<Transaction> findByBuyerIdAndStatus(Long buyerId, String status);
    
    List<Transaction> findBySellerIdAndStatus(Long sellerId, String status);
} 