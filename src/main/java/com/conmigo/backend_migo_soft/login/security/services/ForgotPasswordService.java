package com.conmigo.backend_migo_soft.login.security.services;

import com.conmigo.backend_migo_soft.login.models.User;
import com.conmigo.backend_migo_soft.login.repository.UserRepository;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordService {
    private static final long EXPIRE_TOKEN=30;

    @Autowired
    private UserRepository repo;
    
    @Autowired
    PasswordEncoder encoder;

    public String forgotPass(String email){
        Optional<User> userOptional = Optional.ofNullable(repo.findByEmail(email));

        if(!userOptional.isPresent()){
            return "Invalid email id.";
        }

        User user=userOptional.get();
        user.setToken(generateToken());
        user.setTokenCreationDate(LocalDateTime.now());

        user=repo.save(user);
        return user.getToken();
    }
    
    public String verifyToken(String token){
    
        Optional<User> userOptional= Optional.ofNullable(repo.findByToken(token));

        if(!userOptional.isPresent()){
            return "Invalid token";
        }
        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            return "Token expired.";
        }
        
        return "Valid token";
        
    }

    public String resetPass(String token, String password){
        Optional<User> userOptional= Optional.ofNullable(repo.findByToken(token));

        if(!userOptional.isPresent()){
            return "Invalid token";
        }
        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            return "Token expired.";

        }

        User user = userOptional.get();

        user.setPassword(encoder.encode(password));
        user.setToken(null);
        user.setTokenCreationDate(null);

        repo.save(user);

        return "Your password successfully updated.";
    }

    private String generateToken() {
        String CHARACTERS = "0123456789";
        int TOKEN_LENGTH = 6;
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return token.toString();
    }

    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >=EXPIRE_TOKEN;
    }
    
}
