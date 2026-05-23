package com.perfulandia.autenticacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.autenticacion.model.UsuarioCuenta;

@Repository
    // <UsuarioCuenta, Long> le dice: trabaja con la clase UsuarioCuenta 
    // y su clave primaria es de tipo Long
public interface  UsuarioCuentaRepository extends JpaRepository<UsuarioCuenta, Long> {
    // Optional significa que puede encontrar una UsuarioCuenta o no encontrar nada
    // Es más seguro que devolver null directamente porque evita que el programa crashee
    // Spring genera automáticamente la consulta SQL:
    // SELECT * FROM cuentas_usuario WHERE email_usuario = ?
    Optional<UsuarioCuenta> findByEmailUsuario(String emailUsuario);
    
    //lo mismo de arriba pero en vez de email es el token de acceso
    Optional<UsuarioCuenta> findByTokenDeAcceso(String tokenDeAcceso);

    
}
