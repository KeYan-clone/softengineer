// package org.example.user.controller;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.example.user.dto.LoginRequestDTO;
// import org.example.user.dto.LoginResponseDTO;
// import org.example.backend.core.dto.UserDTO;
// import org.example.user.dto.UserRegistrationDTO;
// import org.example.user.service.UserService;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.Arrays;

// import static org.hamcrest.Matchers.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// class UserControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @MockBean
//     private UserService userService;

//     @Test
//     void testRegisterUser() throws Exception {
//         // 准备测试数据
//         UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
//         registrationDTO.setUsername("testuser");
//         registrationDTO.setPassword("Password123");
//         registrationDTO.setEmail("test@example.com");

//         UserDTO userDTO = new UserDTO();
//         userDTO.setId("1");
//         userDTO.setUsername("testuser");
//         userDTO.setEmail("test@example.com");
//         userDTO.setRole(UserRole.STUDENT);

//         when(userService.registerUser(any(UserRegistrationDTO.class))).thenReturn(userDTO);

//         // 执行测试请求
//         mockMvc.perform(post("/api/users/register")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(registrationDTO)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.code", is(200)))
//                 .andExpect(jsonPath("$.message", is("success")))
//                 .andExpect(jsonPath("$.data.id", is("1")))
//                 .andExpect(jsonPath("$.data.username", is("testuser")))
//                 .andExpect(jsonPath("$.data.email", is("test@example.com")));

//         // 验证服务调用
//         verify(userService).registerUser(any(UserRegistrationDTO.class));
//     }

//     @Test
//     void testLogin() throws Exception {
//         // 准备测试数据
//         LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
//         loginRequestDTO.setUsername("testuser");
//         loginRequestDTO.setPassword("Password123");

//         UserDTO userDTO = new UserDTO();
//         userDTO.setId("1");
//         userDTO.setUsername("testuser");
//         userDTO.setEmail("test@example.com");
//         userDTO.setRole(UserRole.STUDENT);

//         LoginResponseDTO responseDTO = new LoginResponseDTO();
//         responseDTO.setUser(userDTO);
//         responseDTO.setToken("jwt-token");

//         when(userService.login(any(LoginRequestDTO.class))).thenReturn(responseDTO);

//         // 执行测试请求
//         mockMvc.perform(post("/api/users/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(loginRequestDTO)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.code", is(200)))
//                 .andExpect(jsonPath("$.data.user.username", is("testuser")))
//                 .andExpect(jsonPath("$.data.token", is("jwt-token")));

//         // 验证服务调用
//         verify(userService).login(any(LoginRequestDTO.class));
//     }

//     @Test
//     @WithMockUser(username = "testuser")
//     void testGetProfile() throws Exception {
//         // 准备测试数据
//         UserDTO userDTO = new UserDTO();
//         userDTO.setId("1");
//         userDTO.setUsername("testuser");
//         userDTO.setEmail("test@example.com");
//         userDTO.setRole(UserRole.STUDENT);

//         when(userService.getUserByUsername("testuser")).thenReturn(userDTO);

//         // 执行测试请求
//         mockMvc.perform(get("/api/users/profile"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.code", is(200)))
//                 .andExpect(jsonPath("$.data.username", is("testuser")))
//                 .andExpect(jsonPath("$.data.email", is("test@example.com")));

//         // 验证服务调用
//         verify(userService).getUserByUsername("testuser");
//     }

//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void testListUsers() throws Exception {
//         // 准备测试数据
//         UserDTO user1 = new UserDTO();
//         user1.setId("1");
//         user1.setUsername("user1");
        
//         UserDTO user2 = new UserDTO();
//         user2.setId("2");
//         user2.setUsername("user2");
        
//         when(userService.listUsers(eq(1), eq(10)))
//                 .thenReturn(new PageImpl<>(Arrays.asList(user1, user2)));

//         // 执行测试请求
//         mockMvc.perform(get("/api/users")
//                 .param("pageNum", "1")
//                 .param("pageSize", "10"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.code", is(200)))
//                 .andExpect(jsonPath("$.data.list", hasSize(2)))
//                 .andExpect(jsonPath("$.data.list[0].username", is("user1")))
//                 .andExpect(jsonPath("$.data.list[1].username", is("user2")));

//         // 验证服务调用
//         verify(userService).listUsers(1, 10);
//     }
// }
