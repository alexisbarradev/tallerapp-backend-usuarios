package com.taller.usuarioback.controller;

import com.taller.usuarioback.service.UserDetailsServiceImpl;
import com.taller.usuarioback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para el endpoint /login.
 * Recibe las credenciales y devuelve el JWT si es correcto.
 */
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * DTO para recibir el JSON de login.
     */
    public static class LoginRequest {
        private String usuario;
        private String password;

        // Getters y setters
        public String getUsuario() {
            return usuario;
        }
        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * Endpoint POST /login.
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
        try {
            // 1️⃣ Autentica las credenciales
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsuario(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Credenciales inválidas", e);
        }

        // 2️⃣ Carga los detalles del usuario
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsuario());

        // 3️⃣ Genera el token
        final String jwt = jwtUtil.generateToken(userDetails);

        // 4️⃣ Devuelve el token al cliente
        return ResponseEntity.ok(jwt);
    }
}
