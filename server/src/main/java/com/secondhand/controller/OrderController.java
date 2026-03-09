package com.secondhand.controller;

import com.secondhand.dto.OrderCreateRequest;
import com.secondhand.dto.OrderResponse;
import com.secondhand.entity.Product;
import com.secondhand.entity.Transaction;
import com.secondhand.entity.User;
import com.secondhand.service.ProductService;
import com.secondhand.service.TransactionService;
import com.secondhand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderCreateRequest request, Principal principal) {
        User buyer = userService.getUserByUsername(principal.getName());
        Product product = productService.getProductById(request.getProductId());
        User seller = product.getSeller();

        if (seller != null && seller.getId().equals(buyer.getId())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "不能购买自己发布的商品"));
        }

        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setBuyer(buyer);
        transaction.setSeller(seller);
        transaction.setAmount(product.getPrice());
        transaction.setNote("前端下单创建");

        Transaction saved = transactionService.createTransaction(transaction);
        return ResponseEntity.ok(toOrderResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String id) {
        Long transactionId = parseOrderId(id);
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(toOrderResponse(transaction));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());

        List<Transaction> buyerOrders = transactionService.getTransactionsByBuyer(currentUser.getId());
        List<Transaction> sellerOrders = transactionService.getTransactionsBySeller(currentUser.getId());

        Map<Long, Transaction> merged = new LinkedHashMap<>();
        for (Transaction item : buyerOrders) {
            merged.put(item.getId(), item);
        }
        for (Transaction item : sellerOrders) {
            merged.put(item.getId(), item);
        }

        List<OrderResponse> list = new ArrayList<>(merged.values())
                .stream()
                .sorted(Comparator.comparing(Transaction::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toOrderResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/next-step")
    public ResponseEntity<?> nextStep(@PathVariable String id, Principal principal) {
        Long transactionId = parseOrderId(id);
        Transaction transaction = transactionService.getTransactionById(transactionId);
        User currentUser = userService.getUserByUsername(principal.getName());

        boolean canOperate = transaction.getBuyer().getId().equals(currentUser.getId())
                || transaction.getSeller().getId().equals(currentUser.getId());
        if (!canOperate) {
            return ResponseEntity.status(403).body(Collections.singletonMap("message", "无权操作该订单"));
        }

        Transaction updated = transactionService.advanceTransactionStep(transactionId);
        return ResponseEntity.ok(toOrderResponse(updated));
    }

    private Long parseOrderId(String id) {
        if (id != null && id.startsWith("o-")) {
            return Long.parseLong(id.substring(2));
        }
        return Long.parseLong(id);
    }

    private OrderResponse toOrderResponse(Transaction transaction) {
        Product product = transaction.getProduct();

        OrderResponse.OrderItem item = new OrderResponse.OrderItem(
                product == null ? "" : String.valueOf(product.getId()),
                product == null ? "未知商品" : product.getName(),
                1,
                transaction.getAmount()
        );

        OrderResponse.Participant buyer = new OrderResponse.Participant(
                transaction.getBuyer().getId(),
                displayName(transaction.getBuyer())
        );
        OrderResponse.Participant seller = new OrderResponse.Participant(
                transaction.getSeller().getId(),
                displayName(transaction.getSeller())
        );

        return new OrderResponse(
                "o-" + transaction.getId(),
                transaction.getStatus(),
                Collections.singletonList(item),
                transaction.getAmount(),
                buyer,
                seller,
                "平台担保",
                transaction.getCreatedAt() == null ? "" : transaction.getCreatedAt().toString()
        );
    }

    private String displayName(User user) {
        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            return user.getName();
        }
        return user.getUsername();
    }
}
