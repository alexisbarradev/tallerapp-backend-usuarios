package com.taller.usuarioback.repository;

import com.taller.usuarioback.model.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Long> {
    RolUsuario findByNombre(String nombre);
}
