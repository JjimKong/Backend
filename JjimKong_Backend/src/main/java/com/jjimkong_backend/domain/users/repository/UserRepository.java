package com.jjimkong_backend.domain.users.repository;

import com.jjimkong_backend.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
