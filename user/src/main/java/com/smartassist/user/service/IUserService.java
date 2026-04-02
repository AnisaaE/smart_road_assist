package com.smartassist.user.service;

import com.smartassist.user.dto.UserRequestDTO;
import com.smartassist.user.dto.UserResponseDTO;

public interface IUserService {
    UserResponseDTO getUserById(String id);

    UserResponseDTO createUser(UserRequestDTO request);

    UserResponseDTO updateUser(String id, UserRequestDTO request);

    UserResponseDTO updateRole(String id, String role);

    void deleteUser(String id);

    UserResponseDTO updateStatus(String id, String status);
}
