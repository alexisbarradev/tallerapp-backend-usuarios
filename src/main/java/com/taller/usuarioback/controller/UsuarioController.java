package com.taller.usuarioback.controller;

import com.taller.usuarioback.model.Usuario;
import com.taller.usuarioback.service.UsuarioService;
import com.taller.usuarioback.service.S3Service;
import com.taller.usuarioback.model.RolUsuario;
import com.taller.usuarioback.model.EstadoUsuario;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private S3Service s3Service;

    // 🟢 POST /api/registro
    // Crea un nuevo usuario después de validar RUT, correo, usuario, rol y estado.
    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@Valid @RequestBody Usuario usuario) {
        String resultado = usuarioService.crearUsuario(usuario);
        if (resultado.startsWith("Error")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    // 🟢 POST /api/registro-completo
    // Crea un nuevo usuario después de validar RUT, correo, usuario, rol y estado, y maneja la carga de archivos.
    @PostMapping("/registro-completo")
    public ResponseEntity<String> registrarUsuarioCompleto(
            @RequestParam("rut") String rut,
            @RequestParam("primerNombre") String primerNombre,
            @RequestParam("segundoNombre") String segundoNombre,
            @RequestParam("apellidoPaterno") String apellidoPaterno,
            @RequestParam("apellidoMaterno") String apellidoMaterno,
            @RequestParam("direccion") String direccion,
            @RequestParam("usuario") String usuario,
            @RequestParam("correo") String correo,
            @RequestParam("password") String password,
            @RequestParam("rol") String rolJson,
            @RequestParam("estado") String estadoJson,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        
        try {
            // Parse JSON strings to objects
            ObjectMapper mapper = new ObjectMapper();
            RolUsuario rol = mapper.readValue(rolJson, RolUsuario.class);
            EstadoUsuario estado = mapper.readValue(estadoJson, EstadoUsuario.class);

            // Create user object
            Usuario usuarioObj = new Usuario();
            usuarioObj.setRut(rut);
            usuarioObj.setPrimerNombre(primerNombre);
            usuarioObj.setSegundoNombre(segundoNombre);
            usuarioObj.setApellidoPaterno(apellidoPaterno);
            usuarioObj.setApellidoMaterno(apellidoMaterno);
            usuarioObj.setDireccion(direccion);
            usuarioObj.setUsuario(usuario);
            usuarioObj.setCorreo(correo);
            usuarioObj.setPassword(password);
            usuarioObj.setRol(rol);
            usuarioObj.setEstado(estado);

            // Handle file upload if present
            if (file != null && !file.isEmpty()) {
                String fileUrl = s3Service.uploadFile(file);
                usuarioObj.setUrlContrato(fileUrl);
            }

            // Save user
            String resultado = usuarioService.crearUsuario(usuarioObj);
            if (resultado.startsWith("Error")) {
                return ResponseEntity.badRequest().body(resultado);
            }
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    // 🔵 GET /api/usuarios
    // Lista todos los usuarios registrados.
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // 🟡 PUT /api/{id}
    // Actualiza un usuario existente por su ID.
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        String resultado = usuarioService.actualizarUsuario(id, usuarioActualizado);
        if (resultado.startsWith("Error")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    // 🔴 DELETE /api/{id}
    // Elimina un usuario por su ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        String resultado = usuarioService.eliminarUsuario(id);
        if (resultado.startsWith("Error")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    // 🔍 GET /api/usuarios/correo/{correo}
    // Busca un usuario por correo electrónico.
    @GetMapping("/usuarios/correo/{correo}")
    public ResponseEntity<Usuario> buscarPorCorreo(@PathVariable String correo) {
        String correoDecodificado = URLDecoder.decode(correo, StandardCharsets.UTF_8);
        return usuarioService.buscarPorCorreo(correoDecodificado)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // 🔍 GET /api/usuarios/usuario/{nombreUsuario}
    // Busca un usuario por nombre de usuario.
    @GetMapping("/usuarios/usuario/{nombreUsuario}")
    public ResponseEntity<Usuario> buscarPorUsuario(@PathVariable String nombreUsuario) {
        return usuarioService.buscarPorUsuario(nombreUsuario)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // 🔍 GET /api/usuarios/rut/{rut}
    // Busca un usuario por su RUT.
    @GetMapping("/usuarios/rut/{rut}")
    public ResponseEntity<Usuario> buscarPorRut(@PathVariable String rut) {
        return usuarioService.buscarPorRut(rut)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
