package com.smartassist.mechanic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("mechanic_live_state")
public class MechanicLiveState {

    @Id
    private String mechanicId;
    private MechanicStatus status;
    private Double latitude;
    private Double longitude;
}
