package com.example.hackaton.service;

import com.example.hackaton.security.CustomUserDetails;
import com.example.hackaton.web.model.dto.UserAuthDto;
import com.example.hackaton.web.model.dto.UserRegisterDto;
import com.example.hackaton.web.model.dto.UserResponseDto;
import com.example.hackaton.web.model.entity.User;
import com.example.hackaton.repository.UserRepository;
import com.example.hackaton.web.model.entity.pollution.Role;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository contextRepository;

    public UserResponseDto authenticateUser(UserAuthDto userAuthDto,
                                            HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = authenticateUserAndSetContext(userAuthDto, request, response);

        log.info("User {} authenticated", userAuthDto.getEmail());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return new UserResponseDto(userAuthDto.getEmail(), userDetails.getRole().toString());
    }


    public User registerUser(UserRegisterDto userDto,
                             HttpServletRequest request, HttpServletResponse response) {

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), encodedPassword, Role.NOVICE);

        try {
            userRepository.save(user);
            log.info("User {} registered", userDto.getEmail());

            UserAuthDto userAuthDto = new UserAuthDto(userDto.getEmail(), userDto.getPassword());
            authenticateUserAndSetContext(userAuthDto, request, response);

            return user;
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistsException("User " + userDto.getEmail() + " already exists");
        }


    }

    private Authentication authenticateUserAndSetContext(UserAuthDto userAuthDto, HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAuthDto.getEmail(),
                        userAuthDto.getPassword()
                ));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        contextRepository.saveContext(context, request, response);

        return authentication;
    }
}
