package com.reservas.reservasBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reservas.reservasBackend.models.Entities.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {


        Optional<Usuario> findByEmail(String email);
}
