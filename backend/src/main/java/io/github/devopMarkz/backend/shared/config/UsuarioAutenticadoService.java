package io.github.devopMarkz.backend.shared.config;

import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Serviço utilitário responsável por fornecer o usuário atualmente autenticado no contexto de segurança.
 * Esta classe acessa o {@link SecurityContextHolder} do Spring Security para recuperar os dados
 * do usuário autenticado, assumindo que o principal seja do tipo {@link Usuario}.
 */
@Service
public class UsuarioAutenticadoService {

    /**
     * Recupera o usuário atualmente autenticado a partir do contexto de segurança.
     *
     * @return o {@link Usuario} autenticado no momento.
     * @throws ClassCastException se o principal não for do tipo {@link Usuario}.
     */
    public static Usuario obterUsuario() {
        return (Usuario) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
