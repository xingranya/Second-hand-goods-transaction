package com.secondhand.service.impl;

import com.secondhand.entity.Transaction;
import com.secondhand.repository.TransactionRepository;
import com.secondhand.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));
    }

    @Override
    public List<Transaction> getTransactionsByBuyer(Long buyerId) {
        return transactionRepository.findByBuyerId(buyerId);
    }

    @Override
    public List<Transaction> getTransactionsBySeller(Long sellerId) {
        return transactionRepository.findBySellerId(sellerId);
    }

    @Override
    public List<Transaction> getTransactionsByProduct(Long productId) {
        return transactionRepository.findByProductId(productId);
    }

    @Override
    public List<Transaction> getTransactionsByStatus(String status) {
        return transactionRepository.findByStatus(status);
    }

    @Override
    public List<Transaction> getBuyerTransactionsByStatus(Long buyerId, String status) {
        return transactionRepository.findByBuyerIdAndStatus(buyerId, status);
    }

    @Override
    public List<Transaction> getSellerTransactionsByStatus(Long sellerId, String status) {
        return transactionRepository.findBySellerIdAndStatus(sellerId, status);
    }

    @Override
    @Transactional
    public Transaction completeTransaction(Long id) {
        Transaction transaction = getTransactionById(id);
        if (!"PENDING".equals(transaction.getStatus())) {
            throw new RuntimeException("Transaction is not in PENDING status");
        }
        
        transaction.setStatus("COMPLETED");
        transaction.setCompletedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction cancelTransaction(Long id) {
        Transaction transaction = getTransactionById(id);
        if (!"PENDING".equals(transaction.getStatus())) {
            throw new RuntimeException("Transaction is not in PENDING status");
        }
        
        transaction.setStatus("CANCELLED");
        transaction.setCancelledAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = getTransactionById(id);
        transactionRepository.delete(transaction);
    }
} 