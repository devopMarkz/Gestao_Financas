package io.github.devopMarkz.backend.usuario.infraestructure.security;

import io.github.devopMarkz.backend.usuario.domain.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementação personalizada do {@link UserDetailsService} utilizada pelo Spring Security
 * para carregar os dados do usuário durante o processo de autenticação.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor que injeta o repositório de usuários.
     *
     * @param usuarioRepository repositório para buscar usuários por e-mail.
     */
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carrega um usuário com base no nome de usuário (neste caso, o e-mail).
     *
     * @param username o e-mail do usuário a ser autenticado.
     * @return o usuário correspondente, implementando {@link UserDetails}.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
