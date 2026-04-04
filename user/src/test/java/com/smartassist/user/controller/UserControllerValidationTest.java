package com.smartassist.user.controller;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
public class UserControllerValidationTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private IUserService userService;

    @Test
    void whenEmailIsInvalid_thenReturns400() throws Exception {
        String invalidUser = "{\"username\": \"ulku\", \"email\": \"hatali-email\", \"password\": \"12345\"}";
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUser))
                .andExpect(status().isBadRequest());
    }
}