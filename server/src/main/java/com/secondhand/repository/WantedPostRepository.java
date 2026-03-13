package com.secondhand.repository;

import com.secondhand.entity.WantedPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WantedPostRepository extends JpaRepository<WantedPost, Long> {
    List<WantedPost> findAllByOrderByCreatedAtDesc();

    long countByPublisherId(Long publisherId);
}
