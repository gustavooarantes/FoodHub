package com.gustavooarantes.foodhub.service;

import com.gustavooarantes.foodhub.domain.Role;
import com.gustavooarantes.foodhub.domain.User;
import com.gustavooarantes.foodhub.dto.JwtAuthResponseDto;
import com.gustavooarantes.foodhub.dto.LoginDto;
import com.gustavooarantes.foodhub.dto.RegisterDto;
import com.gustavooarantes.foodhub.repository.RoleRepository;
import com.gustavooarantes.foodhub.repository.UserRepository;
import com.gustavooarantes.foodhub.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void registerUser(RegisterDto registerDto) {
        if (userRepository.findByUsername(registerDto.username()).isPresent()) {
            throw new RuntimeException("Erro: Nome de usuário já está em uso!");
        }

        User user = new User();
        user.setUsername(registerDto.username());
        user.setEmail(registerDto.email());

        user.setPassword(passwordEncoder.encode(registerDto.password()));

        Role userRole = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new RuntimeException("Erro: Role não encontrada."));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
    }

    public JwtAuthResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return new JwtAuthResponseDto(token);
    }
}