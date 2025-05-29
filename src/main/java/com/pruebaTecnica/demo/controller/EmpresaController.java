package com.pruebaTecnica.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pruebaTecnica.demo.model.Empresa;
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
@RequestMapping(value = "/Empresa")
public class EmpresaController {
    @Autowired
    private ServicioEmpresa servicioEmpresa;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @GetMapping(value = "/healthCheck")
    public String healthCheck() {
        return "Servidor Ok";
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> listarEmpresas() {
        List<Empresa> empresas = servicioEmpresa.getEmpresas();
        if (empresas.isEmpty()) {
            return new ResponseEntity<>("No hay empresas creadas", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(objectMapper.valueToTree(empresas), HttpStatus.OK);
    }

    public boolean validarCampos(JsonNode jsonNode) {
        if (jsonNode.get("nit").isNumber()
            && jsonNode.get("nombre").isTextual()
            && jsonNode.get("certificado").isTextual()
            && jsonNode.get("flotaActiva").isBoolean()) {
            return true;
        }
        return false;
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> crearEmpresa(@RequestBody JsonNode jsonNode) {
        if (!validarCampos(jsonNode)) {
            return new ResponseEntity<>("No se envio correctamente el objeto", HttpStatus.BAD_REQUEST);
        }

         try {
             Empresa emp = objectMapper.treeToValue(jsonNode, Empresa.class);
             Empresa empresaCreada = servicioEmpresa.crearEmpresa(emp);
             return new ResponseEntity<>(empresaCreada, HttpStatus.OK);
         } catch (Exception e) {
             return new ResponseEntity<>("No se pudo crear la empresa" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }

    @DeleteMapping(value = "/")
    public ResponseEntity<?> eliminarEmpresa(@RequestBody JsonNode jsonNode) {
        if (!validarCampos(jsonNode)) {
            return new ResponseEntity<>("No se envio correctamente el objeto", HttpStatus.BAD_REQUEST);
        }
        try {
            Long id = jsonNode.get("id").asLong();
            Optional<Empresa> empresaOpt = servicioEmpresa.getEmpresaId(id);
            if (empresaOpt.isEmpty()) {
                return new ResponseEntity<>("No se encontro la empresa a eliminar", HttpStatus.BAD_REQUEST);
            }
            Empresa emp = (Empresa) empresaOpt.get();
            Empresa empresaEliminada = servicioEmpresa.eliminarEmpresa(emp);
            return new ResponseEntity<>(empresaEliminada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo eliminar la empresa" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> actualizarEmpresa(@RequestBody JsonNode jsonNode) {
        if (!validarCampos(jsonNode)) {
            return new ResponseEntity<>("No se envio correctamente el objeto", HttpStatus.BAD_REQUEST);
        }

        Long id = jsonNode.get("id").asLong();
        Optional<Empresa> empresaOpt = servicioEmpresa.getEmpresaId(id);
        if (empresaOpt.isEmpty()) {
            return new ResponseEntity<>("No se encontro la empresa a actualizar", HttpStatus.BAD_REQUEST);
        }

        Empresa empVieja = empresaOpt.get();


        try {
            Empresa empNueva = objectMapper.treeToValue(jsonNode, Empresa.class);
            Empresa empresaActualizada = servicioEmpresa.actualizarEmpresa(empNueva, empVieja);
            return new ResponseEntity<>(empresaActualizada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo actualizar la empresa" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
