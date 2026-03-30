package com.smartassist.user.dto;

import lombok.*;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;
    
    // HATEOAS: İstemciye (frontend/dispatcher) bir sonraki adımları söyleyen linkler
    private Map<String, String> _links; 
}