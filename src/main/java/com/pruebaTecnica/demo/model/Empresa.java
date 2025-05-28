package com.pruebaTecnica.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EMPRESA")
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "empresa_seq")
    @SequenceGenerator(name = "empresa_seq", sequenceName = "empresa_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long nit;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String certificado;

    @Column(nullable = false, columnDefinition = "NUMBER(1) DEFAULT 1")
    private boolean flotaActiva;

    @JsonCreator
    public Empresa(@JsonProperty("nit") Long nit,
                   @JsonProperty("nombre") String nombre,
                   @JsonProperty("certificado") String certificado) {
        this.nit = nit;
        this.nombre = nombre;
        this.certificado = certificado;
        this.flotaActiva = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNit() {
        return nit;
    }

    public void setNit(Long nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
    }

    public boolean isFlotaActiva() {
        return flotaActiva;
    }

    public void setFlotaActiva(boolean flotaActiva) {
        this.flotaActiva = flotaActiva;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", nit=" + nit +
                ", nombre='" + nombre + '\'' +
                ", certificado='" + certificado + '\'' +
                ", flotaActiva=" + flotaActiva +
                '}';
    }
}
