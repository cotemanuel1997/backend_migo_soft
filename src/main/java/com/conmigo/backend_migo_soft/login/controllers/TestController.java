package com.conmigo.backend_migo_soft.login.controllers;

import com.conmigo.backend_migo_soft.login.models.Pieza;
import com.conmigo.backend_migo_soft.login.payload.request.PiezaRequest;
import com.conmigo.backend_migo_soft.login.payload.response.MessageResponse;
import com.conmigo.backend_migo_soft.login.repository.PiezaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

//for Angular Client (withCredentials)

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")

//@CrossOrigin(origins = "https://moonlit-hummingbird-c0e029.netlify.app", maxAge = 3600, allowCredentials="true")
//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
  @Autowired
  PiezaRepository piezaRepository;
  
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN') or hasRole(PROFESSIONAL)")
  public String userAccess() {
    return "Contenido vendedor.";
  }

  @GetMapping("/man")
  @PreAuthorize("hasRole('MANAGER')")
  public String moderatorAccess() {
    return "Contenido gerente.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Contenido administrador.";
  }
  
  @GetMapping("/prof")
  @PreAuthorize("hasRole('PROFESSIONAL')")
  public String profAccess() {
    return "Contenido profesional taller.";
  }

  @GetMapping("/getPiezas")
  @PreAuthorize("hasRole('PROFESSIONAL')")
  public List<Pieza> getPieza() {
    return piezaRepository.findAll();
  }
  
  @PostMapping("/regpieza")
  @PreAuthorize("hasRole('PROFESSIONAL') or hasRole('ADMIN')")     
  public ResponseEntity<?> registerPieza(@RequestBody Pieza pieza){
    // Crear pieza nueva
    piezaRepository.save(pieza);
    
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSIONAL') or hasRole('ADMIN')") 
    public ResponseEntity<Void> eliminarPieza(@PathVariable Long id) {
        if (!piezaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        piezaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
