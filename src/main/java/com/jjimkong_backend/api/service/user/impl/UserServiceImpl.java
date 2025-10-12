package com.jjimkong_backend.api.service.user.impl;

import com.jjimkong_backend.api.service.user.UserService;
import com.jjimkong_backend.api.service.user.dto.response.UserResponse;
import com.jjimkong_backend.domain.users.entity.User;
import com.jjimkong_backend.global.config.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final RedisService redisService;

    @Override
    public UserResponse getUserInfo(User user) {
        return UserResponse.fromEntity(user);
    }

    @Override
    @Transactional
    public void logout(User user) {
        redisService.deleteValues(user.getProviderUserId());
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        user.delete();
    }
}