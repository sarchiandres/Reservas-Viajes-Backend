package com.reservas.reservasBackend.models.Dtos;

import lombok.RequiredArgsConstructor;
import lombok.Data;


@Data
@RequiredArgsConstructor
public class JwtResponseDto {

    private final String token;

}
