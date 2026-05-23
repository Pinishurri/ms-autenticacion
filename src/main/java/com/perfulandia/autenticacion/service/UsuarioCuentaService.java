package com.perfulandia.autenticacion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.autenticacion.repository.UsuarioCuentaRepository;

@Service //@Service le dice a Spring que esta clase contiene la lógica del negocio
public class UsuarioCuentaService {
    @Autowired
    private UsuarioCuentaRepository repositorioCuentas;
    
    
    
}
