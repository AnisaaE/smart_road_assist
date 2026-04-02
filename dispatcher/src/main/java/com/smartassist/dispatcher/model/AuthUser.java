package com.smartassist.dispatcher.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "auth_users")
public class AuthUser {

    @Id
    private String id;
    private String email;
    private String passwordHash;
    private String role;
    private String userId;
}
