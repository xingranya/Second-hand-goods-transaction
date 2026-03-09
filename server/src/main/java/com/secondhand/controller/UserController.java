package com.secondhand.controller;

import com.secondhand.dto.UserProfileResponse;
import com.secondhand.dto.VerifyRequest;
import com.secondhand.dto.VerifyResponse;
import com.secondhand.entity.Transaction;
import com.secondhand.entity.User;
import com.secondhand.repository.ProductRepository;
import com.secondhand.repository.TransactionRepository;
import com.secondhand.repository.UserRepository;
import com.secondhand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        int publishCount = productRepository.findBySellerId(user.getId()).size();
        int soldCount = transactionRepository.findBySellerIdAndStatus(user.getId(), "COMPLETED").size();
        Optional<Transaction> latestOrder = transactionRepository.findTopByBuyerIdOrderByCreatedAtDesc(user.getId());

        UserProfileResponse response = new UserProfileResponse(
                user.getId(),
                displayName(user),
                user.getSchool(),
                user.isVerified(),
                publishCount,
                soldCount,
                0,
                latestOrder.map(item -> "o-" + item.getId()).orElse("")
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyResponse> verifyCurrentUser(@Valid @RequestBody VerifyRequest request, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        user.setName(request.getName());
        user.setStudentNo(request.getStudentNo());
        user.setSchool(request.getSchool());
        user.setVerified(true);
        userRepository.save(user);
        return ResponseEntity.ok(new VerifyResponse(true, "认证成功"));
    }

    private String displayName(User user) {
        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            return user.getName();
        }
        return user.getUsername();
    }
}
