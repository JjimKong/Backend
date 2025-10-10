package com.jjimkong_backend.api.service.user;

import com.jjimkong_backend.api.service.user.dto.response.UserResponse;
import com.jjimkong_backend.domain.users.entity.User;

public interface UserService {
    UserResponse getUserInfo(User user);

    void logout(User user);

    void deleteUser(User user);
}
