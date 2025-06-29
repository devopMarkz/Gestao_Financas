package io.github.devopMarkz.backend.services;

import io.github.devopMarkz.backend.exceptions.UsuarioInativoException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class AuthenticationFilterService extends OncePerRequestFilter {

    private final TokenService tokenService;

    public AuthenticationFilterService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = obterTokenDoHeaderDaRequisicao(request);

        if(token != null) {
            var usuario = tokenService.obterUsuario(token);

            if(usuario.getAtivo()) {
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new UsuarioInativoException("Usuário inativo");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String obterTokenDoHeaderDaRequisicao(HttpServletRequest request) {
        var token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        return token.split(" ")[1];
    }

}
