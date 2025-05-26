package com.taller.usuarioback.service;

import com.taller.usuarioback.model.Usuario;
import com.taller.usuarioback.repository.UsuarioRepository;
import com.taller.usuarioback.repository.RolUsuarioRepository;
import com.taller.usuarioback.repository.EstadoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // 🔑 Importamos PasswordEncoder
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolUsuarioRepository rolRepository;

    @Autowired
    private EstadoUsuarioRepository estadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 🔑 Inyectamos PasswordEncoder

    public String crearUsuario(Usuario usuario) {
        if (usuarioRepository.existsByRut(usuario.getRut())) {
            return "Error: El RUT ya existe.";
        }
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            return "Error: El correo ya está registrado.";
        }
        if (usuarioRepository.existsByUsuario(usuario.getUsuario())) {
            return "Error: El nombre de usuario ya está en uso.";
        }
        // Validar que el rol y estado existan
        boolean rolExiste = rolRepository.existsById(usuario.getRol().getId());
        boolean estadoExiste = estadoRepository.existsById(usuario.getEstado().getId());

        if (!rolExiste || !estadoExiste) {
            return "Error: El rol o el estado no existen.";
        }

        // 🔒 Encriptamos la contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        usuarioRepository.save(usuario);
        return "Usuario creado exitosamente.";
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public String actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            return "Error: Usuario no encontrado.";
        }

        Usuario existente = usuarioOptional.get();

        // Actualiza los campos individuales
        existente.setPrimerNombre(usuarioActualizado.getPrimerNombre());
        existente.setSegundoNombre(usuarioActualizado.getSegundoNombre());
        existente.setApellidoPaterno(usuarioActualizado.getApellidoPaterno());
        existente.setApellidoMaterno(usuarioActualizado.getApellidoMaterno());
        existente.setCorreo(usuarioActualizado.getCorreo());
        existente.setUsuario(usuarioActualizado.getUsuario());
        existente.setRut(usuarioActualizado.getRut());
        existente.setRol(usuarioActualizado.getRol());
        existente.setEstado(usuarioActualizado.getEstado());

        // 🔒 Si la contraseña fue modificada, encriptarla antes de guardarla
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }

        usuarioRepository.save(existente);

        return "Usuario actualizado correctamente.";
    }

    public String eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            return "Error: Usuario no encontrado.";
        }
        usuarioRepository.deleteById(id);
        return "Usuario eliminado correctamente.";
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public Optional<Usuario> buscarPorUsuario(String nombreUsuario) {
        return usuarioRepository.findByUsuario(nombreUsuario);
    }

    public Optional<Usuario> buscarPorRut(String rut) {
        return usuarioRepository.findByRut(rut);
    }
}
