package com.reservas.reservasBackend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservas.reservasBackend.Jwt.JwtUtil;
import com.reservas.reservasBackend.models.Dtos.UsuarioDto;
import com.reservas.reservasBackend.models.Entities.Usuario;
import com.reservas.reservasBackend.services.UsuarioServices;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioServices usuarioServices;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/registrar", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registrarUsuario(@Validated @RequestBody UsuarioDto usuarioDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getFieldErrors()
                    .stream()
                    .collect(java.util.stream.Collectors.toMap(
                            fe -> fe.getField(),
                            fe -> fe.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("validationErrors", errors));
        }

        try {
            Map<String, Object> response = usuarioServices.saveUser(usuarioDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> getPerfil(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        if (token == null)
            return ResponseEntity.status(401).build();
        Claims claims = jwtUtil.extractAllClaims(token);
        Long userId = Long.valueOf(claims.get("id").toString());
        return ResponseEntity.ok(usuarioServices.getUserById(userId));
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping()
    public ResponseEntity<?> getUsers(HttpServletRequest request) {
        List<Usuario> users = usuarioServices.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {

        try {
            Usuario response = usuarioServices.updateUser(id, usuarioDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/editar")
    public ResponseEntity<?> updateMyUser(HttpServletRequest request, @RequestBody UsuarioDto usuarioDto) {
        try {

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of("error", "Token no proporcionado"));
            }

            String token = authHeader.substring(7);
            Claims claims = jwtUtil.extractAllClaims(token);

            Long userId = Long.valueOf(claims.get("id").toString());

            Usuario updatedUser = usuarioServices.updateUser(userId, usuarioDto);
            String newToken = jwtUtil.generateToken(updatedUser);
            return ResponseEntity.ok(Map.of(
                    "user", updatedUser,
                    "token", newToken));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
