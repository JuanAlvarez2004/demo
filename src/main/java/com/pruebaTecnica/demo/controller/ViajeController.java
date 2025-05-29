package com.pruebaTecnica.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pruebaTecnica.demo.model.Empresa;
import com.pruebaTecnica.demo.model.Viaje;
import com.pruebaTecnica.demo.service.ServicioViaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.tags.HtmlEscapeTag;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/Viaje")
public class ViajeController {

    @Autowired
    private ServicioViaje servicioViaje;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public boolean validarCampos(JsonNode jsonNode) {
        if (jsonNode.get("nombre_vehiculo").isTextual()
                && jsonNode.get("capacidad_pasajeros").isNumber()
                && jsonNode.get("destino").isTextual()
                && jsonNode.get("origen").isTextual()) {
            return true;
        }
        return false;
    }

    @GetMapping(value = "/healthCheck")
    public String healthCheck() {
        return "Servidor Ok";
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> crearViaje(@RequestBody JsonNode jsonNode) {
        if (!validarCampos(jsonNode)) {
            return ResponseEntity.badRequest().body("No se envio correctamente el objeto");
        }

        JsonNode empresaNode = jsonNode.get("empresa");

        try {
            Empresa empresa = objectMapper.treeToValue(empresaNode, Empresa.class);

            Viaje viaje = objectMapper.treeToValue(jsonNode, Viaje.class);
            viaje.setEmpresa(empresa);
            Viaje viajeCreado = servicioViaje.crearViaje(viaje);
            return new ResponseEntity<>(viajeCreado, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el viaje: " + e.getMessage());
        }
    }
}
