package com.secondhand.controller;

import com.secondhand.entity.Transaction;
import com.secondhand.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByBuyer(@PathVariable Long buyerId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBuyer(buyerId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Transaction>> getTransactionsBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(transactionService.getTransactionsBySeller(sellerId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Transaction>> getTransactionsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(transactionService.getTransactionsByProduct(productId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Transaction>> getTransactionsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(transactionService.getTransactionsByStatus(status));
    }

    @GetMapping("/buyer/{buyerId}/status/{status}")
    public ResponseEntity<List<Transaction>> getBuyerTransactionsByStatus(
            @PathVariable Long buyerId,
            @PathVariable String status) {
        return ResponseEntity.ok(transactionService.getBuyerTransactionsByStatus(buyerId, status));
    }

    @GetMapping("/seller/{sellerId}/status/{status}")
    public ResponseEntity<List<Transaction>> getSellerTransactionsByStatus(
            @PathVariable Long sellerId,
            @PathVariable String status) {
        return ResponseEntity.ok(transactionService.getSellerTransactionsByStatus(sellerId, status));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Transaction> completeTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.completeTransaction(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Transaction> cancelTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.cancelTransaction(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
} 