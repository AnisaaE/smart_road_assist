package com.smartassist.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.smartassist.user.dto.UserResponseDTO;
import com.smartassist.user.dto.UserRequestDTO;
import com.smartassist.user.exception.UserNotFoundException;
import com.smartassist.user.model.User;
import com.smartassist.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        return convertToResponseDTO(user);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(request.getRole())
                .status("ACTIVE") // Varsayılan durum
                .build();
        return convertToResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        user.setStatus(status);
        return convertToResponseDTO(userRepository.save(user));
    }

    // Mapper Metodu: Entity -> DTO (RMM Level 3 Hazırlığı)
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