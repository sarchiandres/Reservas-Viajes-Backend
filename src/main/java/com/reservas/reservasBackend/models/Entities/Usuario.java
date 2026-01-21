package com.reservas.reservasBackend.models.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "usarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String name ; 

    @Column(name = "email", nullable = false)
    private String email;

    @Column ( name = "contraseña", nullable = false)
    private String password;

    
    @Column ( name = "rol" ,columnDefinition = "Enum('admin','cliente')" ,nullable = false  )
    private String role;
}
