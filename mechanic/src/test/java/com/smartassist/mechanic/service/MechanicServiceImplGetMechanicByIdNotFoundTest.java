package com.smartassist.mechanic.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartassist.mechanic.exception.MechanicNotFoundException;
import com.smartassist.mechanic.mapper.MechanicMapper;
import com.smartassist.mechanic.repository.MechanicRepository;
import com.smartassist.mechanic.service.impl.MechanicServiceImpl;

@ExtendWith(MockitoExtension.class)
class MechanicServiceImplGetMechanicByIdNotFoundTest {

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private MechanicMapper mechanicMapper;

    @InjectMocks
    private MechanicServiceImpl mechanicService;

    @Test
    void getMechanicByIdShouldThrowWhenMechanicDoesNotExist() {
        when(mechanicRepository.findById("missing-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mechanicService.getMechanicById("missing-id"))
                .isInstanceOf(MechanicNotFoundException.class)
                .hasMessage("Mechanic not found: missing-id");
    }
}
