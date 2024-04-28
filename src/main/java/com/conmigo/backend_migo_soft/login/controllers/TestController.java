package com.conmigo.backend_migo_soft.login.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//for Angular Client (withCredentials)
@CrossOrigin(origins = "https://moonlit-hummingbird-c0e029.netlify.app", maxAge = 3600, allowCredentials="true")
//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
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
}
