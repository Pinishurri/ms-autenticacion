package com.perfulandia.autenticacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "cuentas_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCuenta {
    //genera automaticamente las id/Prim_key
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    // @NotBlank significa que no puede llegar vacío ni con solo espacios
    // @Email significa que tiene que tener formato de email válido (contener @ y dominio)
    // @Size define el largo mínimo y máximo permitido del texto
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    @Column(nullable = false, unique = true, length = 100)
    private String emailUsuario;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    @Column(nullable = false)   
    private String contrasenia;


    // Estado actual de la cuenta: ACTIVA, BLOQUEADA o INACTIVA
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private EstadoDeCuenta estadoDeCuenta = EstadoDeCuenta.ACTIVA;

    public enum EstadoDeCuenta{
        ACTIVA,
        BLOQUEADA,
        INACTIVA
    }



    

}
