package com.reservas.reservasBackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservas.reservasBackend.Jwt.JwtUtil;
import com.reservas.reservasBackend.models.Dtos.JwtResponseDto;
import com.reservas.reservasBackend.models.Dtos.LoginDto;
import com.reservas.reservasBackend.models.Entities.Usuario;
import com.reservas.reservasBackend.services.UsuarioServices;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioServices usuarioServices;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> postMethodName(@RequestBody LoginDto dto) {
        try {
            Usuario user = usuarioServices.findUserEmail(dto.getEmail());
            if (user.getPassword() == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
            }
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(new JwtResponseDto(token));
        } catch (RuntimeException e) {
           
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }

    }

}
