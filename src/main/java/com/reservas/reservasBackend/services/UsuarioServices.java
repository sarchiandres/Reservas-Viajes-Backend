package com.reservas.reservasBackend.services;

import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.reservas.reservasBackend.models.Dtos.UsuarioDto;
import com.reservas.reservasBackend.models.Entities.Usuario;
import com.reservas.reservasBackend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServices {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> saveUser(UsuarioDto usuarioDto) {
        if (usuarioRepository.findByCedula(usuarioDto.getCedula()).isPresent()) {
            throw new RuntimeException("El cedula ya está en uso : " + usuarioDto.getCedula());
        }

        String typeUser = usuarioDto.getRole();
        if (!List.of("admin", "cliente").contains(typeUser)) {
            throw new RuntimeException("El rol no es válido: " + typeUser);

        }

        Usuario usuario = new Usuario();
        usuario.setName(usuarioDto.getName());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setCedula(usuarioDto.getCedula());
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        usuario.setRole(typeUser);
        usuarioRepository.save(usuario);

        return Map.of("message", "Usuario registrado exitosamente");
    }

    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    public Usuario getUserById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
    }

    public Usuario updateUser(Long id, UsuarioDto updatedUser) {
        Usuario existingUser = getUserById(id);
        if ((updatedUser.getName() != null) && (updatedUser.getEmail() != null)
                && (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty())
                && (updatedUser.getRole() != null)) {

            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return usuarioRepository.save(existingUser);
    }

    public Usuario findUserEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
    }

    public void deleteUser(Long id) {
        Usuario usuario = getUserById(id);
        usuarioRepository.delete(usuario);
    }
}
