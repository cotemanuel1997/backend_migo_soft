package com.conmigo.backend_migo_soft.login.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;
 
public class PiezaRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String nombre;
 
    @NotBlank
    @Size(max = 50)
    private String materialidad;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String taller;

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
  

}
