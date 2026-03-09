package com.secondhand.controller;

import com.secondhand.dto.WantedCreateRequest;
import com.secondhand.dto.WantedPostResponse;
import com.secondhand.entity.User;
import com.secondhand.entity.WantedPost;
import com.secondhand.repository.WantedPostRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wanted")
public class WantedController {

    @Autowired
    private WantedPostRepository wantedPostRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<WantedPostResponse>> getWantedPosts() {
        List<WantedPostResponse> list = wantedPostRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<WantedPostResponse> createWantedPost(@Valid @RequestBody WantedCreateRequest request, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        WantedPost post = new WantedPost();
        post.setTitle(request.getTitle());
        post.setExpectedPrice(request.getExpectedPrice());
        post.setDescription(request.getDescription());
        post.setCampus(request.getCampus());
        post.setPublisher(currentUser);
        if (request.getDeadline() != null && !request.getDeadline().trim().isEmpty()) {
            post.setDeadline(LocalDate.parse(request.getDeadline()));
        }

        WantedPost saved = wantedPostRepository.save(post);
        return ResponseEntity.ok(toResponse(saved));
    }

    private WantedPostResponse toResponse(WantedPost post) {
        WantedPostResponse.Publisher publisher = new WantedPostResponse.Publisher(
                post.getPublisher().getId(),
                displayName(post.getPublisher())
        );
        return new WantedPostResponse(
                "w-" + post.getId(),
                post.getTitle(),
                post.getExpectedPrice(),
                post.getDeadline() == null ? "" : post.getDeadline().toString(),
                post.getDescription(),
                post.getCampus(),
                publisher,
                post.getCreatedAt() == null ? "" : post.getCreatedAt().toString()
        );
    }

    private String displayName(User user) {
        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            return user.getName();
        }
        return user.getUsername();
    }
}
