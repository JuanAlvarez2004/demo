package com.pruebaTecnica.demo.service;

import com.pruebaTecnica.demo.model.Empresa;
import com.pruebaTecnica.demo.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioEmpresa {

    @Autowired
    private EmpresaRepository empresaRepository;

    // CRUD
    //Create
    @Transactional
    public Empresa crearEmpresa(Empresa emp) throws Exception{
        if (emp != null && empresaRepository.findByNit(emp.getNit()).isEmpty()) {
            return empresaRepository.save(emp);
        } else {
            throw new Exception("La empresa no puede ser nula o no puede tener el mismo NIT que otra empresa existente");
        }
    }

    //Delete
    @Transactional
    public Empresa eliminarEmpresa(Empresa emp) throws Exception {
        if (emp != null && empresaRepository.findByNit(emp.getNit()).isPresent()) {
            emp.setFlotaActiva(false);
            return empresaRepository.save(emp);
        } else {
            throw new Exception("La empresa no puede ser nula o debe existir en la base de datos");
        }
    }

    //Read
    @Transactional(readOnly = true)
    public List<Empresa> getEmpresas() {
        return empresaRepository.findByFlotaActivaTrue();
    }

    //Update
    @Transactional
    public Empresa actualizarEmpresa(Empresa empNueva, Empresa empVieja) throws Exception {

        if (!empresaRepository.existsByNitAndFlotaActivaTrueAndIdNot(empNueva.getNit(), empVieja.getId())) {
            empNueva.setId(empVieja.getId());
            empNueva.setFlotaActiva(true);
            return empresaRepository.save(empNueva);
        } else {
            throw new Exception("No se puede actualizar una empresa con el NIT de una empresa existente");
        }
    }

    @Transactional
    public Optional<Empresa> getEmpresaId(Long id) {
        return empresaRepository.findById(id);
    }

}
