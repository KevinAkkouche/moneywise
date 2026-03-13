package com.moneywise.service;

import com.moneywise.dto.*;
import com.moneywise.entity.User;
import com.moneywise.repository.UserRepository;
import com.moneywise.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà pris");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserType(User.UserType.valueOf(request.getUserType().toUpperCase()));

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getUsername());
        return new AuthResponse(token, user.getUsername(), user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getUserType().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        String token = jwtUtils.generateToken(user.getUsername());
        return new AuthResponse(token, user.getUsername(), user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getUserType().name());
    }
}
