package com.smartassist.user.service;

import java.util.List;

import com.smartassist.user.dto.UserRequestDTO;
import com.smartassist.user.dto.UserResponseDTO;

public interface IUserService {
    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(String id);

    UserResponseDTO createUser(UserRequestDTO request);

    UserResponseDTO updateUser(String id, UserRequestDTO request);

    UserResponseDTO updateRole(String id, String role);

    void deleteUser(String id);

    UserResponseDTO updateStatus(String id, String status);
}
