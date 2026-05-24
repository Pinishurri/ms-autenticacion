package com.perfulandia.autenticacion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.autenticacion.model.UsuarioCuenta;
import com.perfulandia.autenticacion.model.UsuarioCuenta.EstadoDeCuenta;
import com.perfulandia.autenticacion.repository.UsuarioCuentaRepository;



@Service //@Service le dice a Spring que esta clase contiene la lógica del negocio

public class UsuarioCuentaService {
    @Autowired
    private UsuarioCuentaRepository repositorioCuentas;

    //REGISTRAR USUARIO nuevo: recibe datos y guarda cuenta en la base de datos
    public UsuarioCuenta registrarUsuario(UsuarioCuenta datosCuenta){
        datosCuenta.setEstadoDeCuenta(EstadoDeCuenta.ACTIVA);
        //save() guarda el objeto en la base de datos
        return repositorioCuentas.save(datosCuenta);
    }
    //INICIAR SESION : VERIFICA QUE MAIL Y CLAVE SEAN CORRECTAS


    
    
    
}
