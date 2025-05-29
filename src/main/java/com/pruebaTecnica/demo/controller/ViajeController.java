package com.pruebaTecnica.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pruebaTecnica.demo.model.Empresa;
import com.pruebaTecnica.demo.model.Viaje;
import com.pruebaTecnica.demo.service.ServicioEmpresa;
import com.pruebaTecnica.demo.service.ServicioViaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/Viaje")
public class ViajeController {

    @Autowired
    private ServicioViaje servicioViaje;

    @Autowired
    private ServicioEmpresa servicioEmpresa;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public boolean validarCampos(JsonNode jsonNode) {
        return jsonNode.has("nombre_vehiculo") && jsonNode.get("nombre_vehiculo").isTextual()
                && jsonNode.has("capacidad_pasajeros") && jsonNode.get("capacidad_pasajeros").isNumber()
                && jsonNode.has("destino") && jsonNode.get("destino").isTextual()
                && jsonNode.has("origen") && jsonNode.get("origen").isTextual()
                && jsonNode.has("empresa_id") && jsonNode.get("empresa_id").isNumber();
    }

    public boolean validarCamposActualizacion(JsonNode jsonNode) {
        return jsonNode.has("id") && jsonNode.get("id").isNumber()
                && validarCampos(jsonNode);
    }

    @GetMapping(value = "/healthCheck")
    public String healthCheck() {
        return "Servidor Ok";
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> listarViajes() {
        try {
            List<Viaje> viajes = servicioViaje.getViajes();
            if (viajes.isEmpty()) {
                return new ResponseEntity<>("No hay viajes creados", HttpStatus.NOT_FOUND);
            }
            ArrayNode arrayNode = objectMapper.valueToTree(viajes);
            return new ResponseEntity<>(arrayNode, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener los viajes: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> crearViaje(@RequestBody JsonNode jsonNode) {
        if (!validarCampos(jsonNode)) {
            return ResponseEntity.badRequest().body("No se envió correctamente el objeto. " +
                    "Campos requeridos: nombre_vehiculo, capacidad_pasajeros, destino, origen, empresa_id");
        }

        try {
            Long empresaId = jsonNode.get("empresa_id").asLong();

            // Buscar la empresa existente
            Optional<Empresa> empresaOpt = servicioEmpresa.getEmpresaId(empresaId);
            if (empresaOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("La empresa con ID " + empresaId + " no existe");
            }

            Empresa empresa = empresaOpt.get();
            if (!empresa.isFlotaActiva()) {
                return ResponseEntity.badRequest().body("La empresa debe tener la flota activa para crear viajes");
            }

            // Crear el viaje manualmente extrayendo solo los campos necesarios
            Viaje viaje = new Viaje(
                    jsonNode.get("nombre_vehiculo").asText(),
                    jsonNode.get("capacidad_pasajeros").asInt(),
                    jsonNode.get("origen").asText(),
                    jsonNode.get("destino").asText()
            );
            // Asignar la empresa existente
            viaje.setEmpresa(empresa);

            Viaje viajeCreado = servicioViaje.crearViaje(viaje);
            return new ResponseEntity<>(viajeCreado, HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el viaje: " + e.getMessage());
        }
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> actualizarViaje(@RequestBody JsonNode jsonNode) {
        if (!validarCamposActualizacion(jsonNode)) {
            return ResponseEntity.badRequest().body("No se envió correctamente el objeto. " +
                    "Campos requeridos: id, nombre_vehiculo, capacidad_pasajeros, destino, origen, empresa_id");
        }

        try {
            Long viajeId = jsonNode.get("id").asLong();
            Long empresaId = jsonNode.get("empresa_id").asLong();

            // Verificar que el viaje existe
            Optional<Viaje> viajeOpt = servicioViaje.getViajeById(viajeId);
            if (viajeOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("El viaje con ID " + viajeId + " no existe");
            }

            // Verificar que la empresa existe y está activa
            Optional<Empresa> empresaOpt = servicioEmpresa.getEmpresaId(empresaId);
            if (empresaOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("La empresa con ID " + empresaId + " no existe");
            }

            Empresa empresa = empresaOpt.get();
            if (!empresa.isFlotaActiva()) {
                return ResponseEntity.badRequest().body("La empresa debe tener la flota activa");
            }

            // Crear el viaje manualmente extrayendo solo los campos necesarios
            Viaje viajeNuevo = new Viaje(
                    jsonNode.get("nombre_vehiculo").asText(),
                    jsonNode.get("capacidad_pasajeros").asInt(),
                    jsonNode.get("origen").asText(),
                    jsonNode.get("destino").asText()
            );
            // Asignar la empresa existente
            viajeNuevo.setEmpresa(empresa);

            Viaje viajeActualizado = servicioViaje.actualizarViaje(viajeNuevo, viajeOpt.get());
            return new ResponseEntity<>(viajeActualizado, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el viaje: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/")
    public ResponseEntity<?> eliminarViaje(@RequestBody JsonNode jsonNode) {
        if (!jsonNode.has("id") || !jsonNode.get("id").isNumber()) {
            return ResponseEntity.badRequest().body("Debe proporcionar un ID válido del viaje");
        }

        try {
            Long viajeId = jsonNode.get("id").asLong();

            Optional<Viaje> viajeOpt = servicioViaje.getViajeById(viajeId);
            if (viajeOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("El viaje con ID " + viajeId + " no existe");
            }

            Viaje viaje = viajeOpt.get();
            Viaje viajeEliminado = servicioViaje.eliminarViaje(viaje);
            return new ResponseEntity<>(viajeEliminado, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el viaje: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> obtenerViaje(@PathVariable Long id) {
        try {
            Optional<Viaje> viajeOpt = servicioViaje.getViajeById(id);
            if (viajeOpt.isEmpty()) {
                return new ResponseEntity<>("Viaje no encontrado", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(viajeOpt.get(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el viaje: " + e.getMessage());
        }
    }
}