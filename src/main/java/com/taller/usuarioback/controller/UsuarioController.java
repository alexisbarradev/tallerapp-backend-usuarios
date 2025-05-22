package com.taller.usuarioback.controller;

import com.taller.usuarioback.model.Usuario;
import com.taller.usuarioback.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 🟢 POST /api/registro
    // Crea un nuevo usuario después de validar RUT, correo, nombre de usuario, rol y estado.
    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@Valid @RequestBody Usuario usuario) {
        String resultado = usuarioService.crearUsuario(usuario);

        if (resultado.startsWith("Error")) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }

    // 🔵 GET /api/usuarios
    // Retorna una lista con todos los usuarios registrados.
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // 🟡 PUT /api/{id}
    // Actualiza los datos de un usuario identificado por su ID.
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        String resultado = usuarioService.actualizarUsuario(id, usuarioActualizado);

        if (resultado.startsWith("Error")) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }

    // 🔴 DELETE /api/{id}
    // Elimina un usuario existente por ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        String resultado = usuarioService.eliminarUsuario(id);

        if (resultado.startsWith("Error")) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }
}
