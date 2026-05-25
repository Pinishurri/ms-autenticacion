package com.perfulandia.autenticacion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// DTO para iniciar sesión
// Solo acepta email y contraseña
@Data
public class LoginDTO {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    private String emailUsuario;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String contrasenia;
}
