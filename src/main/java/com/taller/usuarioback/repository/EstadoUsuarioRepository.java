package com.taller.usuarioback.repository;

import com.taller.usuarioback.model.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoUsuarioRepository extends JpaRepository<EstadoUsuario, Long> {
    EstadoUsuario findByEstado(String estado);
}
