package com.perfulandia.autenticacion.service;


import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.autenticacion.model.UsuarioCuenta;
import com.perfulandia.autenticacion.model.UsuarioCuenta.EstadoDeCuenta;
import com.perfulandia.autenticacion.repository.UsuarioCuentaRepository;



@Service //@Service le dice a Spring que esta clase contiene la lógica del negocio

public class UsuarioCuentaService {

    // Logger es el que registra los mensajes en la consola de forma estructurada
    // LoggerFactory.getLogger le dice a qué clase pertenecen esos mensajes
    private static final Logger log = LoggerFactory.getLogger(UsuarioCuentaService.class);

    @Autowired
    private UsuarioCuentaRepository repositorioCuentas;

    //REGISTRAR USUARIO 

    public UsuarioCuenta registrarUsuario(UsuarioCuenta datosCuenta){
        log.info("intentando registrar un usuario con email: {}", datosCuenta.getEmailUsuario());
        datosCuenta.setEstadoDeCuenta(EstadoDeCuenta.ACTIVA);
        UsuarioCuenta cuentaGuardada = repositorioCuentas.save(datosCuenta);
        log.info("usuario registrado correctamente con id:  {}", cuentaGuardada.getIdUsuario());
        return cuentaGuardada;
        
    }


    //INICIAR SESION 


    public Optional<UsuarioCuenta> iniciarSesion (String emailIngresado, String contraseniaIngresada){
        log.info("intento de inicio de sesion para el email: {} ", emailIngresado);

        //BUSCAMOS AL USUARIO POR MAIL EN LA BD
        Optional<UsuarioCuenta> Buscado = repositorioCuentas.findByEmailUsuario(emailIngresado);
        //SI NO EXISTE SE RETORNA VACIO
        if (Buscado.isEmpty()){
            log.warn("inicio de sesion fallido: no existe el email {}",emailIngresado);
            return Optional.empty();
        }
        //acá guarda la variable en cuentaEncontrada
        UsuarioCuenta cuentaEncontrada = Buscado.get();

        //si la cuenta está bloqueada no la dejamos entrar 

        if (cuentaEncontrada.getEstadoDeCuenta() == EstadoDeCuenta.BLOQUEADA){
            log.warn("inicio de sesión fallido: cuenta bloqueada para email {}",emailIngresado);
            return Optional.empty();
        }

        //se verifica si la clave ingresada coincide con la guardada
        boolean contraseniaCorrecta = cuentaEncontrada
                        .getContrasenia()
                        .equals(contraseniaIngresada);
         //si la clave es incorrecta retornamos vacio

         if (!contraseniaCorrecta){
            log.error("inicio de sesion fallido: contraseña incorrecta para el email {}",emailIngresado);
            return Optional.empty();
         }

         //si está todo bien, retornamos la cuenta encontrada
         log.info("Inicio de sesión exitoso para email: {}", emailIngresado);
         return Optional.of(cuentaEncontrada);
    }
    //CERRAR SESION

    public boolean cerrarSesion(String emailDelUsuario){
        log.info("cerrando sesion para el email: {}", emailDelUsuario);

        Optional<UsuarioCuenta> busqueda = repositorioCuentas.findByEmailUsuario(emailDelUsuario);

        if(busqueda.isEmpty()){
            log.warn("cierre de sesion fallido: no existe el email {}", emailDelUsuario);
            return false;
        }

        log.info("sesion cerrada exitosamente para el email {}", emailDelUsuario);
        return true;
    }

    //RECUPERAR CONTRASEÑA

    public boolean recuperarContrasena(String emailDelUsuario) {
        log.info("Solicitud de recuperación de contraseña para email: {}", emailDelUsuario);

        Optional<UsuarioCuenta> busqueda = repositorioCuentas
                .findByEmailUsuario(emailDelUsuario);

        if (busqueda.isEmpty()) {
            log.warn("Recuperación fallida: no existe el email {}", emailDelUsuario);
            return false;
        }

        // Simulamos el envío del email
        log.info("Simulando envío de email de recuperación a: {}", emailDelUsuario);
        return true;
    }

    // BLOQUEAR CUENTA
    public boolean bloquearCuenta(Long idUsuarioABloquear) {
        log.info("Intentando bloquear cuenta con id: {}", idUsuarioABloquear);

        Optional<UsuarioCuenta> busqueda = repositorioCuentas.findById(idUsuarioABloquear);

        if (busqueda.isEmpty()) {
            log.warn("Bloqueo fallido: no existe usuario con id {}", idUsuarioABloquear);
            return false;
        }

        UsuarioCuenta cuentaEncontrada = busqueda.get();
        cuentaEncontrada.setEstadoDeCuenta(EstadoDeCuenta.BLOQUEADA);
        repositorioCuentas.save(cuentaEncontrada);
        log.info("Cuenta bloqueada exitosamente para id: {}", idUsuarioABloquear);
        return true;
    }

    //VALIDAR TOKEN ( VERIFICA QUE EL USUARIO EXISTE)

    public boolean validarToken(String emailDelUsuario) {
        log.info("Validando token para email: {}", emailDelUsuario);
        Optional<UsuarioCuenta> busqueda = repositorioCuentas
                .findByEmailUsuario(emailDelUsuario);
        return busqueda.isPresent();
    }

    //LISTAR TODOS LOS USUARIOS 
    public List<UsuarioCuenta> listarUsuarios() {
        log.info("Listando todos los usuarios del sistema");
        return repositorioCuentas.findAll();
    }


    
    
    
}
