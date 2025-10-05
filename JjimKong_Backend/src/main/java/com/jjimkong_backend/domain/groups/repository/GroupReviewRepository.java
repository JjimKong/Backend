package com.jjimkong_backend.domain.groups.repository;

import com.jjimkong_backend.domain.groups.entity.GroupReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupReviewRepository extends JpaRepository<GroupReview, Long> {
}
