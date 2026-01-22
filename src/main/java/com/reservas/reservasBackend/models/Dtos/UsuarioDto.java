package com.reservas.reservasBackend.models.Dtos;

import lombok.Data;

@Data
public class UsuarioDto {
   
    private String name ; 
    private String email;
    private String password;
    private String role;
    private long cedula;

}
