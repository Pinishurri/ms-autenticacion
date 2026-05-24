package com.perfulandia.autenticacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    //acá especifica que el email es unico, no puede ni repetirse ni estar vacio
    @Column (nullable=false, unique=true, length=100)
    private String emailUsuario;

    //clave protegida, nunca se guarda en texto plano
    @Column (nullable=false)    
    private String contrasenia;

    @Column (nullable=false)
    private int cantIntentosFallidos=0;

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
