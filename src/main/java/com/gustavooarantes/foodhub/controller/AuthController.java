package com.gustavooarantes.foodhub.controller;

import com.gustavooarantes.foodhub.dto.RegisterDto;
import com.gustavooarantes.foodhub.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        authService.registerUser(registerDto);
        return new ResponseEntity<>("Usuário registrado com sucesso!", HttpStatus.CREATED);
    }
}