package com.smartassist.dispatcher.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JwtService {

    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String jwtSecret;

    public JwtService(@Value("${dispatcher.jwt-secret:test-jwt-secret-with-sufficient-length-123456}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public String generateToken(String email, String userId, String role) {
        try {
            String header = encodeJson(Map.of("alg", "HS256", "typ", "JWT"));
            String payload = encodeJson(buildPayload(email, userId, role));
            String signature = sign(header + "." + payload);
            return header + "." + payload + "." + signature;
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize JWT payload", exception);
        }
    }

    private Map<String, Object> buildPayload(String email, String userId, String role) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", email);
        payload.put("userId", userId);
        payload.put("role", role);
        payload.put("iat", Instant.now().getEpochSecond());
        return payload;
    }

    private String encodeJson(Map<String, Object> content) throws JsonProcessingException {
        return URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(content));
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return URL_ENCODER.encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Could not sign JWT", exception);
        }
    }
}
