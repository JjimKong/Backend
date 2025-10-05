package com.jjimkong_backend.domain.groups.repository;

import com.jjimkong_backend.domain.groups.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
