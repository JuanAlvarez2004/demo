package com.pruebaTecnica.demo.service;

import com.pruebaTecnica.demo.model.Viaje;
import com.pruebaTecnica.demo.repository.EmpresaRepository;
import com.pruebaTecnica.demo.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioViaje {
    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    //CRUD
    //Create
    @Transactional
    public Viaje crearViaje(Viaje viaje) throws Exception {
        if (viaje != null && empresaRepository.findByIdAndFlotaActivaTrue(viaje.getEmpresa().getId()).isPresent()) {
            return viajeRepository.save(viaje);
        } else {
            throw new Exception("El viaje no puede ser nulo y la flota debe existir y estar activa");
        }
    }

    //Read
    @Transactional(readOnly = true)
    public List<Viaje> getViajes() throws Exception {
        return viajeRepository.findAll();
    }

    //Read by ID
    @Transactional(readOnly = true)
    public Optional<Viaje> getViajeById(Long id) {
        return viajeRepository.findById(id);
    }

    //Update
    @Transactional
    public Viaje actualizarViaje(Viaje viajeNuevo, Viaje viajeViejo) throws Exception {
        if (viajeNuevo != null && empresaRepository.findByIdAndFlotaActivaTrue(viajeNuevo.getEmpresa().getId()).isPresent()) {
            viajeNuevo.setId(viajeViejo.getId());
            return viajeRepository.save(viajeNuevo);
        } else {
            throw new Exception("El viaje no puede ser nulo y la empresa debe existir y estar activa");
        }
    }

    //Delete
    //El viaje solo se puede borrar si la flota esta inactiva
    @Transactional
    public Viaje eliminarViaje(Viaje viaje) throws Exception {
        if (empresaRepository.existsByIdAndFlotaActivaTrue(viaje.getEmpresa().getId())) {
            throw new Exception("Solo puede eliminar viajes de empresas con flota inactiva");
        }
        viajeRepository.delete(viaje);
        return viaje;
    }
}