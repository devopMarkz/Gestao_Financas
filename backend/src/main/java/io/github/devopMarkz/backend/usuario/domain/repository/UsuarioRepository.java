package io.github.devopMarkz.backend.usuario.domain.repository;

import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

}
