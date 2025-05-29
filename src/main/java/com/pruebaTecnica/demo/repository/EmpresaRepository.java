package com.pruebaTecnica.demo.repository;

import com.pruebaTecnica.demo.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByNit(Long nit);

    Optional<Empresa> findByIdAndFlotaActivaTrue(Long id);

    List<Empresa> findByFlotaActivaTrue();

    boolean existsByIdAndFlotaActivaTrue(Long id);

    boolean existsByNit(Long nit);

    @Query("SELECT COUNT(e) > 0 FROM Empresa e WHERE e.nit = :nit AND e.flotaActiva = true AND e.id != :id")
    boolean existsByNitAndFlotaActivaTrueAndIdNot(@Param("nit") Long nit, @Param("id") Long id);
}
