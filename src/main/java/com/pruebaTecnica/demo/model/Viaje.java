package com.pruebaTecnica.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VIAJE")
@NoArgsConstructor
@AllArgsConstructor
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "viaje_seq")
    @SequenceGenerator(name = "viaje_seq", sequenceName = "viaje_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nombreVehiculo;

    @Column(nullable = false)
    private int capacidadPasajeros;

    @Column(nullable = false)
    private String origen;

    @Column(nullable = false)
    private String destino;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    // Constructor con @JsonCreator - FUNCIONA PERFECTAMENTE
    @JsonCreator
    public Viaje(@JsonProperty("nombre_vehiculo") String nombreVehiculo,
                 @JsonProperty("capacidad_pasajeros") int capacidadPasajeros,
                 @JsonProperty("origen") String origen,
                 @JsonProperty("destino") String destino) {
        this.nombreVehiculo = nombreVehiculo;
        this.capacidadPasajeros = capacidadPasajeros;
        this.origen = origen;
        this.destino = destino;
    }

    // Constructor completo para uso interno
    public Viaje(String nombreVehiculo, int capacidadPasajeros, String origen, String destino, Empresa empresa) {
        this.nombreVehiculo = nombreVehiculo;
        this.capacidadPasajeros = capacidadPasajeros;
        this.origen = origen;
        this.destino = destino;
        this.empresa = empresa;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreVehiculo() {
        return nombreVehiculo;
    }

    public void setNombreVehiculo(String nombreVehiculo) {
        this.nombreVehiculo = nombreVehiculo;
    }

    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public void setCapacidadPasajeros(int capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Viaje{" +
                "id=" + id +
                ", nombreVehiculo='" + nombreVehiculo + '\'' +
                ", capacidadPasajeros=" + capacidadPasajeros +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", empresa=" + (empresa != null ? empresa.getNombre() : "null") +
                '}';
    }
}