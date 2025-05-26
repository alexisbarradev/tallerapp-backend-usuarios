package com.taller.usuarioback.service;

import com.taller.usuarioback.model.Usuario;
import com.taller.usuarioback.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Esta clase implementa UserDetailsService de Spring Security.
 * Su tarea es buscar en la base de datos al usuario (por nombre de usuario)
 * y devolver un UserDetails con sus credenciales para que Spring Security pueda autenticarlo.
 * 
 * Validamos también que el usuario tenga el rol de "ADMIN" (por nombre),
 * para permitir el acceso sólo a los administradores.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al usuario en la base de datos
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Validamos que el rol del usuario sea "ADMIN" (usando el nombre del rol, no el id)
        if (!"ADMIN".equalsIgnoreCase(usuario.getRol().getNombre())) {
            throw new UsernameNotFoundException("El usuario no tiene permisos de administrador.");
        }

        // Creamos y devolvemos el UserDetails (con usuario, contraseña y lista vacía de authorities)
        return new org.springframework.security.core.userdetails.User(
                usuario.getUsuario(),
                usuario.getPassword(),
                Collections.emptyList() // Aquí podrías usar roles si quieres
        );
    }
}
