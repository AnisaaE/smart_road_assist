package com.smartassist.mechanic.model;

import java.time.Instant;
import java.util.List;

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
@Document(collection = "mechanic_profiles")
public class MechanicProfile {

    @Id
    private String id;
    private String name;
    private String phoneNumber;
    private List<String> specialties;
    private String serviceArea;
    private Instant createdAt;
}
