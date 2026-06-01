package com.perfulandia.autenticacion.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.autenticacion.dto.LoginDTO;
import com.perfulandia.autenticacion.dto.RegistroUsuarioDTO;
import com.perfulandia.autenticacion.model.UsuarioCuenta;
import com.perfulandia.autenticacion.service.UsuarioCuentaService;

import jakarta.validation.Valid;

// @RestController indica que esta clase maneja peticiones HTTP
// @RequestMapping define la ruta base de todos los endpoints
@RestController
@RequestMapping("/api/autenticacion")
public class UsuarioCuentaController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioCuentaController.class);

    // Spring conecta automáticamente el service aquí
    @Autowired
    private UsuarioCuentaService servicioUsuarios;

    // POST /api/autenticacion/registro
    // @Valid activa las validaciones que pusimos en el DTO
    // @RequestBody convierte el JSON que llega en un objeto Java
    @PostMapping("/registro")
    public ResponseEntity<UsuarioCuenta> registrar(@Valid @RequestBody RegistroUsuarioDTO datosRegistro) {
        log.info("Solicitud de registro para email: {}", datosRegistro.getEmailUsuario());

        // Convertimos el DTO a una entidad UsuarioCuenta
        // El DTO solo tiene email y contrasenia, el resto lo completa el service
        UsuarioCuenta nuevaCuenta = new UsuarioCuenta();
        nuevaCuenta.setEmailUsuario(datosRegistro.getEmailUsuario());
        nuevaCuenta.setContrasenia(datosRegistro.getContrasenia());

        try {
            UsuarioCuenta cuentaGuardada = servicioUsuarios.registrarUsuario(nuevaCuenta);
            return new ResponseEntity<>(cuentaGuardada, HttpStatus.CREATED); // 201
        } catch (Exception error) {
            log.error("Error al registrar usuario: {}", error.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 si el email ya existe
        }
    }

    // POST /api/autenticacion/login
    @PostMapping("/login")
    public ResponseEntity<UsuarioCuenta> login(@Valid @RequestBody LoginDTO datosLogin) {
        log.info("Solicitud de login para email: {}", datosLogin.getEmailUsuario());

        Optional<UsuarioCuenta> resultado = servicioUsuarios.iniciarSesion(
                datosLogin.getEmailUsuario(),
                datosLogin.getContrasenia()
        );

        if (resultado.isPresent()) {
            return new ResponseEntity<>(resultado.get(), HttpStatus.OK); // 200
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
        }
    }

    // POST /api/autenticacion/logout/{email}
    // @PathVariable recibe el email directamente desde la URL
    // Ejemplo: /api/autenticacion/logout/juan@gmail.com
    @PostMapping("/logout/{email}")
    public ResponseEntity<String> cerrarSesion(@PathVariable String email) {
        log.info("Solicitud de cierre de sesión para email: {}", email);

        boolean exitoso = servicioUsuarios.cerrarSesion(email);

        if (exitoso) {
            return new ResponseEntity<>("Sesión cerrada correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No existe una cuenta con ese email", HttpStatus.NOT_FOUND);
        }
    }

    // POST /api/autenticacion/recuperar-contrasenia/{email}
    // Ejemplo: /api/autenticacion/recuperar-contrasenia/juan@gmail.com
    @PostMapping("/recuperar-contrasenia/{email}")
    public ResponseEntity<String> recuperarContrasenia(@PathVariable String email) {
        log.info("Solicitud de recuperación de contraseña para email: {}", email);

        boolean exitoso = servicioUsuarios.recuperarContrasena(email);

        if (exitoso) {
            return new ResponseEntity<>("Email de recuperación enviado", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No existe una cuenta con ese email", HttpStatus.NOT_FOUND);
        }
    }

    // GET /api/autenticacion/validar-token/{email}
    // Lo usan los otros microservicios para verificar si
    // un usuario existe en el sistema
    // Ejemplo: /api/autenticacion/validar-token/juan@gmail.com
    @GetMapping("/validar-token/{email}")
    public ResponseEntity<Boolean> validarToken(@PathVariable String email) {
        log.info("Solicitud de validación de token para email: {}", email);

        boolean esValido = servicioUsuarios.validarToken(email);
        return new ResponseEntity<>(esValido, HttpStatus.OK);
    }

    // PUT /api/autenticacion/bloquear/{id}
    // @PathVariable recibe el id directamente desde la URL
    // Ejemplo: /api/autenticacion/bloquear/3
    @PutMapping("/bloquear/{id}")
    public ResponseEntity<String> bloquearCuenta(@PathVariable Long id) {
        log.info("Solicitud de bloqueo de cuenta con id: {}", id);

        boolean exitoso = servicioUsuarios.bloquearCuenta(id);

        if (exitoso) {
            return new ResponseEntity<>("Cuenta bloqueada correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No existe una cuenta con ese id", HttpStatus.NOT_FOUND);
        }
    }

    // GET /api/autenticacion/usuarios
    // Lista todos los usuarios, útil para el Administrador
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioCuenta>> listarUsuarios() {
        log.info("Solicitud para listar todos los usuarios");
        List<UsuarioCuenta> usuarios = servicioUsuarios.listarUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
}