package com.smartassist.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smartassist.user.dto.UserRequestDTO;
import com.smartassist.user.dto.UserResponseDTO;
import com.smartassist.user.exception.UserNotFoundException;
import com.smartassist.user.model.User;
import com.smartassist.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(String id) {
        return convertToResponseDTO(findUserOrThrow(id));
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(request.getRole())
                .status("ACTIVE")
                .build();
        return convertToResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO request) {
        User user = findUserOrThrow(id);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        return convertToResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateRole(String id, String role) {
        User user = findUserOrThrow(id);
        user.setRole(role);
        return convertToResponseDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO updateStatus(String id, String status) {
        User user = findUserOrThrow(id);
        
        // Testin GREEN olması için gereken kontrol:
        // Eğer gelen statü geçerli listede yoksa hata fırlat
        List<String> validStatuses = List.of("ACTIVE", "BUSY", "OFFLINE");
        if (!validStatuses.contains(status)) {
            throw new com.smartassist.user.exception.InvalidStatusException("Invalid status: " + status);
        }

        user.setStatus(status);
        return convertToResponseDTO(userRepository.save(user));
    }

    private User findUserOrThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}
