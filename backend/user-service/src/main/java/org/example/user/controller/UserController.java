package org.example.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.backend.common.web.PageResult;
import org.example.backend.common.web.Result;
import org.example.user.dto.LoginRequestDTO;
import org.example.user.dto.LoginResponseDTO;
import org.example.backend.core.dto.UserDTO;
import org.example.user.dto.UserRegistrationDTO;
import org.example.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 用户API控制器
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户相关API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户")
    public Result<UserDTO> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserDTO userDTO = userService.registerUser(registrationDTO);
        return Result.success(userDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录并获取令牌")
    public Result<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = userService.login(loginRequestDTO);
        return Result.success(response);
    }

    @GetMapping("/profile")
    @Operation(summary = "获取个人资料", description = "获取当前登录用户的个人资料")
    public Result<UserDTO> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        UserDTO userDTO = userService.getUserByUsername(userDetails.getUsername());
        return Result.success(userDTO);
    }

    @PutMapping("/profile")
    @Operation(summary = "更新个人资料", description = "更新当前登录用户的个人资料")
    public Result<UserDTO> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                        @Valid @RequestBody UserDTO userDTO) {
        UserDTO currentUser = userService.getUserByUsername(userDetails.getUsername());
        UserDTO updatedUser = userService.updateUser(currentUser.getId(), userDTO);
        return Result.success(updatedUser);
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码", description = "修改当前登录用户的密码")
    public Result<Void> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam String oldPassword,
                                     @RequestParam String newPassword) {
        userService.changePassword(userDetails.getUsername(), oldPassword, newPassword);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取指定用户", description = "根据ID获取用户信息")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<UserDTO> getUser(@PathVariable String id) {
        UserDTO userDTO = userService.getUserById(id);
        return Result.success(userDTO);
    }

    @GetMapping
    @Operation(summary = "分页查询用户", description = "分页查询用户列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<UserDTO>> listUsers(@RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        Page<UserDTO> page = userService.listUsers(pageNum, pageSize);
        
        PageResult<UserDTO> pageResult = new PageResult<>();
        pageResult.setList(page.getContent());
        pageResult.setTotal(page.getTotalElements());
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        
        return Result.success(pageResult);
    }
}
