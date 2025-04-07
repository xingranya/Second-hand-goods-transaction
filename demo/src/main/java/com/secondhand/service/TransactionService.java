package com.secondhand.service;

import com.secondhand.entity.Transaction;
import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    
    Transaction getTransactionById(Long id);
    
    List<Transaction> getTransactionsByBuyer(Long buyerId);
    
    List<Transaction> getTransactionsBySeller(Long sellerId);
    
    List<Transaction> getTransactionsByProduct(Long productId);
    
    List<Transaction> getTransactionsByStatus(String status);
    
    List<Transaction> getBuyerTransactionsByStatus(Long buyerId, String status);
    
    List<Transaction> getSellerTransactionsByStatus(Long sellerId, String status);
    
    Transaction completeTransaction(Long id);
    
    Transaction cancelTransaction(Long id);
    
    void deleteTransaction(Long id);
} 