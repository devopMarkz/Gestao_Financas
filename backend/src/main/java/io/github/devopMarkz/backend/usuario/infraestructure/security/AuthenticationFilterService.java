package io.github.devopMarkz.backend.usuario.infraestructure.security;

import io.github.devopMarkz.backend.usuario.infraestructure.exception.UsuarioInativoException;
import io.github.devopMarkz.backend.usuario.infraestructure.security.handlers.CustomAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticação personalizado que intercepta as requisições HTTP,
 * extrai o token JWT do cabeçalho e autentica o usuário no contexto do Spring Security.
 */
@Service
public class AuthenticationFilterService extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    /**
     * Construtor para injeção de dependências.
     *
     * @param tokenService serviço responsável por extrair e validar tokens.
     * @param customAuthenticationEntryPoint manipulador de resposta para falhas de autenticação.
     */
    public AuthenticationFilterService(TokenService tokenService, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.tokenService = tokenService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    /**
     * Metodo executado uma vez por requisição. Verifica a presença do token JWT no cabeçalho,
     * valida o token e autentica o usuário no contexto do Spring Security.
     *
     * @param request     a requisição HTTP.
     * @param response    a resposta HTTP.
     * @param filterChain cadeia de filtros que será continuada após este filtro.
     * @throws ServletException em caso de falha no processamento da requisição.
     * @throws IOException em caso de erro de I/O.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = obterTokenDoHeaderDaRequisicao(request);

            if (token != null) {
                var usuario = tokenService.obterUsuario(token);

                if (usuario.getAtivo()) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            usuario.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new UsuarioInativoException("Usuário inativo");
                }
            }
        } catch (UsuarioInativoException e) {
            customAuthenticationEntryPoint.commence(request, response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token JWT do cabeçalho "Authorization" da requisição HTTP.
     *
     * @param request a requisição HTTP.
     * @return o token sem o prefixo "Bearer ", ou {@code null} se o cabeçalho estiver ausente ou mal formatado.
     */
    private String obterTokenDoHeaderDaRequisicao(HttpServletRequest request) {
        var token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        return token.split(" ")[1];
    }
}
