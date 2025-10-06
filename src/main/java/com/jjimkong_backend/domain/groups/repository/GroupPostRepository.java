package com.jjimkong_backend.domain.groups.repository;

import com.jjimkong_backend.domain.groups.entity.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {
}
