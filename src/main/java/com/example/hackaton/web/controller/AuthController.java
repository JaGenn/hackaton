package com.example.hackaton.web.controller;

import com.example.hackaton.security.CustomUserDetails;
import com.example.hackaton.web.model.dto.UserAuthDto;
import com.example.hackaton.web.model.dto.UserRegisterDto;
import com.example.hackaton.web.model.dto.UserResponseDto;
import com.example.hackaton.web.model.entity.User;
import com.example.hackaton.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "User Authentication API")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/sign-in")
    @Operation(summary = "User login")
    public ResponseEntity<UserResponseDto> signIn(@RequestBody UserAuthDto userAuthDto,
                                                  HttpServletRequest request, HttpServletResponse response) {
        log.info("POST /api/auth/sign-in username: {}", userAuthDto.getEmail());
        UserResponseDto responseDto = authService.authenticateUser(userAuthDto, request, response);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "User registration")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRegisterDto userDto,
                                                  HttpServletRequest request, HttpServletResponse response) {
        log.info("POST /api/auth/sign-up username: {}", userDto.getEmail());
        User createdUser = authService.registerUser(userDto, request, response);
        UserResponseDto responseDto = new UserResponseDto(createdUser.getEmail(), createdUser.getRole().toString());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/sign-out")
    @Operation(summary = "User logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> signOut() {
        return ResponseEntity.noContent().build();
    }


}