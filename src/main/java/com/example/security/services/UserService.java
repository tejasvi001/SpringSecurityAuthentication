package com.example.security.services;

import com.example.security.dtos.LoginDTO;
import com.example.security.dtos.SignUpDTO;
import com.example.security.dtos.UserDTO;
import com.example.security.entities.User;
import com.example.security.exceptions.ResourceNotFoundException;
import com.example.security.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.modelMapper=modelMapper;
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userRepository.findByEmail(username).orElseThrow(
                    ()->new ResourceNotFoundException("no user with the email "+username+" exists")
            );
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDTO signUp(SignUpDTO signUpDTO) {
        Optional<User> user=userRepository.findByEmail(signUpDTO.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User with email already exists "+signUpDTO.getEmail());
        }
        User toCreate=modelMapper.map(signUpDTO,User.class);
        toCreate.setPassword(passwordEncoder.encode(toCreate.getPassword()));
        User savedUser=userRepository.save(toCreate);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public User getUserById(Long userId) throws ResourceNotFoundException {
        return  userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("no user with the id  "+userId+" exists")
        );
    }
}
