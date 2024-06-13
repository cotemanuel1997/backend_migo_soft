package com.conmigo.backend_migo_soft.login.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "piezas")
public class Pieza {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id_pieza;

  @NotBlank
  @Size(max = 20)
  private String nombre;

  @NotBlank
  @Size(max = 50)
  private String materialidad;

  @NotBlank
  @Size(max = 120)
  private String taller;

  @NotBlank
  @Size(max = 120)
  private String estado;    
  
  @NotBlank
  @Size(max = 120)
  private String terminacion;

  public Pieza() {
  }

    public Pieza(Long id_pieza, String nombre, String materialidad, String taller, String estado, String terminacion) {
        this.id_pieza = id_pieza;
        this.nombre = nombre;
        this.materialidad = materialidad;
        this.taller = taller;
        this.estado = estado;
        this.terminacion = terminacion;
    }

    public Long getId_pieza() {
        return id_pieza;
    }

    public void setId_pieza(Long id_pieza) {
        this.id_pieza = id_pieza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMaterialidad() {
        return materialidad;
    }

    public void setMaterialidad(String materialidad) {
        this.materialidad = materialidad;
    }

    public String getTaller() {
        return taller;
    }

    public void setTaller(String taller) {
        this.taller = taller;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTerminacion() {
        return terminacion;
    }

    public void setTerminacion(String terminacion) {
        this.terminacion = terminacion;
    }


  
  
}
