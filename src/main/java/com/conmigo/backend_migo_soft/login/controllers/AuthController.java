package com.conmigo.backend_migo_soft.login.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conmigo.backend_migo_soft.login.models.ERole;
import com.conmigo.backend_migo_soft.login.models.Role;
import com.conmigo.backend_migo_soft.login.models.User;
import com.conmigo.backend_migo_soft.login.payload.request.EmailRequest;
import com.conmigo.backend_migo_soft.login.payload.request.LoginRequest;
import com.conmigo.backend_migo_soft.login.payload.request.SignupRequest;
import com.conmigo.backend_migo_soft.login.payload.response.JwtResponse;
import com.conmigo.backend_migo_soft.login.payload.response.MessageResponse;
import com.conmigo.backend_migo_soft.login.repository.RoleRepository;
import com.conmigo.backend_migo_soft.login.repository.UserRepository;
import com.conmigo.backend_migo_soft.login.security.jwt.JwtUtils;
import com.conmigo.backend_migo_soft.login.security.services.EmailService;
import com.conmigo.backend_migo_soft.login.security.services.ForgotPasswordService;
import com.conmigo.backend_migo_soft.login.security.services.UserDetailsImpl;
import java.util.Optional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;

@CrossOrigin(origins = "https://moonlit-hummingbird-c0e029.netlify.app", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;
  
  @Autowired
  ForgotPasswordService forgotPassService;
  
  @Autowired
  EmailService emailService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, 
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), 
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "man":
          Role manRole = roleRepository.findByName(ERole.ROLE_MANAGER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(manRole);
          
        case "prof":
                  Role profRole = roleRepository.findByName(ERole.ROLE_PROFESSIONAL)
                      .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                  roles.add(profRole);          

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
  
    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPass( @RequestParam String email){
        String response = forgotPassService.forgotPass(email);

        if(!response.startsWith("Invalid")){
            Context context = new Context();
            context.setVariable("message", response);

            emailService.sendEmailWithHtmlTemplate(email, "Recuperar contraseña", "email-template", context);
            return ResponseEntity.ok(new MessageResponse(response));
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Email inválido"));        
        
    }
    
    @GetMapping("/verify-token")
        public ResponseEntity<?> verifyToken(@RequestParam String token){
            String response = forgotPassService.verifyToken(token);
            if(response == "Invalid token"){
                return ResponseEntity
                         .badRequest()
                         .body(new MessageResponse("Token invalido"));                
            }
            if(response == "Token expired."){
                 return ResponseEntity
                         .badRequest()
                         .body(new MessageResponse("Token expirado"));                                       
            }
            return ResponseEntity.ok(new MessageResponse("Token válido")); 
           
    }    

    @PutMapping("/reset-password")
        public ResponseEntity<?> resetPass(@RequestParam String token, @RequestParam String password){
            String response = forgotPassService.resetPass(token,password);
            if(response == "Invalid token"){
                return ResponseEntity
                         .badRequest()
                         .body(new MessageResponse("Token invalido"));                
            }
            if(response == "Token expired."){
                 return ResponseEntity
                         .badRequest()
                         .body(new MessageResponse("Token expirado"));                                       
            }
            return ResponseEntity.ok(new MessageResponse("Contraseña actualizada")); 
    }
        



    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
        return "Email sent successfully!";
    }
    
    @PostMapping("/send-html-email")
    public String sendHtmlEmail(@RequestBody EmailRequest emailRequest) {
        Context context = new Context();
        context.setVariable("message", emailRequest.getBody());

        emailService.sendEmailWithHtmlTemplate(emailRequest.getTo(), emailRequest.getSubject(), "email-template", context);
        return "HTML email sent successfully!";
    }
}

