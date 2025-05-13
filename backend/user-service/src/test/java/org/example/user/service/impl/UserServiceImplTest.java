// package org.example.user.service.impl;

// import org.example.user.domain.User;
// import org.example.backend.core.dto.UserDTO;
// import org.example.backend.core.repository.UserRepository;
// import org.example.user.dto.UserRegistrationDTO;
// import org.example.user.exception.UserException;
// import org.example.backend.common.security.JwtTokenUtil;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.core.userdetails.UserDetailsService;

// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;

// class UserServiceImplTest {

//     @Mock
//     private UserRepository userRepository;

//     @Mock
//     private PasswordEncoder passwordEncoder;

//     @Mock
//     private AuthenticationManager authenticationManager;

//     @Mock
//     private UserDetailsService userDetailsService;

//     @Mock
//     private JwtTokenUtil jwtTokenUtil;

//     @InjectMocks
//     private UserServiceImpl userService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testRegisterUser_Success() {
//         // 准备测试数据
//         UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
//         registrationDTO.setUsername("testuser");
//         registrationDTO.setPassword("Password123");
//         registrationDTO.setEmail("test@example.com");
//         registrationDTO.setRole(UserRole.STUDENT);

//         // 模拟依赖行为
//         when(userRepository.existsByUsername(anyString())).thenReturn(false);
//         when(userRepository.existsByEmail(anyString())).thenReturn(false);
//         when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

//         User savedUser = new User();
//         savedUser.setId("1");
//         savedUser.setUsername("testuser");
//         savedUser.setEmail("test@example.com");
//         savedUser.setPassword("encodedPassword");
//         savedUser.setRole(UserRole.STUDENT);

//         when(userRepository.save(any(User.class))).thenReturn(savedUser);

//         // 执行测试
//         UserDTO result = userService.registerUser(registrationDTO);

//         // 验证结果
//         assertNotNull(result);
//         assertEquals("testuser", result.getUsername());
//         assertEquals("test@example.com", result.getEmail());
//         assertEquals(UserRole.STUDENT, result.getRole());

//         // 验证交互
//         verify(userRepository).existsByUsername("testuser");
//         verify(userRepository).existsByEmail("test@example.com");
//         verify(passwordEncoder).encode("Password123");
//         verify(userRepository).save(any(User.class));
//     }

//     @Test
//     void testRegisterUser_UsernameExists() {
//         // 准备测试数据
//         UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
//         registrationDTO.setUsername("existinguser");
//         registrationDTO.setPassword("Password123");
//         registrationDTO.setEmail("test@example.com");

//         // 模拟用户名已存在
//         when(userRepository.existsByUsername("existinguser")).thenReturn(true);

//         // 执行测试并验证异常
//         UserException exception = assertThrows(UserException.class, () -> {
//             userService.registerUser(registrationDTO);
//         });

//         assertEquals(UserException.USERNAME_ALREADY_EXISTS, exception.getCode());
//         verify(userRepository).existsByUsername("existinguser");
//         verify(userRepository, never()).save(any(User.class));
//     }

//     @Test
//     void testGetUserById_Success() {
//         // 准备测试数据
//         User user = new User();
//         user.setId("1");
//         user.setUsername("testuser");
//         user.setEmail("test@example.com");
//         user.setRole(UserRole.STUDENT);

//         // 模拟依赖行为
//         when(userRepository.findById("1")).thenReturn(Optional.of(user));

//         // 执行测试
//         UserDTO result = userService.getUserById("1");

//         // 验证结果
//         assertNotNull(result);
//         assertEquals("1", result.getId());
//         assertEquals("testuser", result.getUsername());
//         assertEquals("test@example.com", result.getEmail());
//         assertEquals(UserRole.STUDENT, result.getRole());

//         // 验证交互
//         verify(userRepository).findById("1");
//     }

//     @Test
//     void testGetUserById_NotFound() {
//         // 模拟用户不存在
//         when(userRepository.findById("999")).thenReturn(Optional.empty());

//         // 执行测试并验证异常
//         UserException exception = assertThrows(UserException.class, () -> {
//             userService.getUserById("999");
//         });

//         assertEquals(UserException.USER_NOT_FOUND, exception.getCode());
//         verify(userRepository).findById("999");
//     }
// }
