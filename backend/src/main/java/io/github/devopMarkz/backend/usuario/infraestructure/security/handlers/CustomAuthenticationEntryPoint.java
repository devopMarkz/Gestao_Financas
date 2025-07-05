package io.github.devopMarkz.backend.usuario.infraestructure.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.devopMarkz.backend.shared.dto.ErroDTO;
import io.github.devopMarkz.backend.usuario.infraestructure.exception.UsuarioInativoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Manipulador personalizado para erros de autenticação (HTTP 401).
 *
 * Essa classe intercepta requisições que falham na autenticação (por exemplo, token inválido ou ausente)
 * e retorna uma resposta padronizada em JSON com os detalhes do erro.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Metodo invocado automaticamente pelo Spring Security quando a autenticação falha.
     * Retorna um JSON contendo o status, a mensagem de erro, o caminho da requisição e o timestamp.
     *
     * @param request objeto da requisição HTTP.
     * @param response objeto da resposta HTTP.
     * @param authException exceção lançada durante a tentativa de autenticação.
     * @throws IOException caso ocorra erro ao escrever a resposta.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Captura a causa original da exceção, se houver
        Throwable causa = (Throwable) request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        // Define a mensagem padrão da exceção
        String mensagem = authException.getMessage();

        // Se a causa for um usuário inativo, ajusta a mensagem com base na exceção específica
        if (causa instanceof UsuarioInativoException e) {
            mensagem = e.getMessage();
        }

        // Configura a resposta HTTP como 401 (Unauthorized)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Cria o DTO de erro contendo os detalhes do problema
        ErroDTO erroDTO = new ErroDTO(
                Instant.now(),
                401,
                mensagem,
                request.getRequestURI()
        );

        // Converte o DTO para JSON e escreve no corpo da resposta
        String json = new ObjectMapper().writeValueAsString(erroDTO);
        response.getWriter().write(json);
    }
}
