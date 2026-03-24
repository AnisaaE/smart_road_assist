package com.smartassist.user;

import com.smartassist.user.model.User;
// import com.smartassist.user.service.UserService; // Burayı henüz açma ki KIRMIZI yansın!
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    // Senin yazdığın kod (Modelin sağlam olduğunu kanıtlar)
    @Test
    void testUserCreation() {
        User user = new User("1", "Ulku", "ulku@test.com", "USER");
        assertEquals("Ulku", user.getName());
    }

    // Benim eklediğim kod (Sistemin 'BEYNİNİN' olmadığını kanıtlar - GERÇEK RED)
    @Test
    void shouldCreateUserSuccessfully() {
        // BURASI KIRMIZI YANMALI (UserService sınıfı henüz oluşturulmadı)
        UserService userService = new UserService(); 
        
        User user = new User("1", "Ulku", "ulku@test.com", "USER");
        User savedUser = userService.saveUser(user);
        
        assertNotNull(savedUser);
        assertEquals("Ulku", savedUser.getName());
    }
}