package com.pruebaTecnica.demo.controller;

import com.pruebaTecnica.demo.service.ServicioEmpresa;
import com.pruebaTecnica.demo.service.ServicioViaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/Empresa")
public class EmpresaController {
    @Autowired
    private ServicioEmpresa servicioEmpresa;

    @Autowired
    private ServicioViaje servicioViaje;

    @GetMapping(value = "/healthCheck")
    public String healthCheck() {
        return "Servidor Ok";
    }

}
