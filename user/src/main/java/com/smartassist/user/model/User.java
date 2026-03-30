package com.smartassist.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users") // MongoDB'deki tablo ismi

public class User {
    @Id
    private String id; // NoSQL'de ID genelde String/ObjectId olur
    private String name;
    private String email;
    private String phone;
    private String role; // ADMIN, USER, MECHANIC, DISPATCHER
    private String status; // ACTIVE, INACTIVE, BANNED gibi durumlar için
}