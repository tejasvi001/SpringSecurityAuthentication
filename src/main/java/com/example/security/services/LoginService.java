package com.example.security.services;

import com.example.security.dtos.LoginDTO;
import com.example.security.dtos.LoginResponseDTO;
import com.example.security.entities.User;
import com.example.security.exceptions.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;
    public LoginService(AuthenticationManager authenticationManager, JWTService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public LoginResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword())
        );

        User user=(User) authentication.getPrincipal();
        String accessToken= jwtService.generateAccessToken(user);
        String refreshToken= jwtService.generateRefreshToken(user);
        return  new LoginResponseDTO(user.getId(), accessToken,refreshToken);
    }

    public LoginResponseDTO refreshToken(String refreshToken)  {
        Long userId= jwtService.getUserIdFromToken(refreshToken);
        User user= null;
        try {
            user = userService.getUserById(userId);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
        String accessToken= jwtService.generateAccessToken(user);
        return  new LoginResponseDTO(user.getId(), accessToken,refreshToken);

    }
}
