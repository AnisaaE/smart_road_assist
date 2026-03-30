package com.smartassist.user.service;

import com.smartassist.user.dto.UserRequestDTO;
import com.smartassist.user.dto.UserResponseDTO;    
import java.util.List;

public interface IUserService {
    UserResponseDTO getUserById(String id);
    UserResponseDTO createUser(UserRequestDTO request);
    
    // EKSİK OLANLAR:
    UserResponseDTO updateUser(String id, UserRequestDTO request);
    void deleteUser(String id);
    UserResponseDTO updateStatus(String id, String status); // Planımızda vardı
}