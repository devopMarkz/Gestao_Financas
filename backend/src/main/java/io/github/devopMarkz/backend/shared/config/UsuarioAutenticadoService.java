package io.github.devopMarkz.backend.shared.config;

import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioAutenticadoService {

    public static Usuario obterUsuario(){
        return (Usuario) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
