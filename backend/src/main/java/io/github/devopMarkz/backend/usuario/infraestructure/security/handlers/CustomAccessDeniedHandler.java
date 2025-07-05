package io.github.devopMarkz.backend.usuario.infraestructure.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.devopMarkz.backend.shared.dto.ErroDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Manipulador personalizado para erros de acesso negado (HTTP 403).
 *
 * Esta classe é responsável por interceptar tentativas de acesso a recursos
 * protegidos por parte de usuários autenticados, mas que não possuem as permissões necessárias.
 * Ao invés de retornar a resposta padrão do Spring Security, ela retorna um JSON estruturado
 * contendo detalhes do erro.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Manipula a exceção {@link AccessDeniedException} lançada quando um usuário tenta acessar
     * um recurso sem as permissões adequadas.
     *
     * @param request objeto da requisição HTTP.
     * @param response objeto da resposta HTTP.
     * @param accessDeniedException exceção lançada quando o acesso é negado.
     * @throws IOException em caso de erro ao escrever a resposta.
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        // Define o status HTTP 403 (Forbidden)
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Cria o objeto de erro com as informações relevantes
        ErroDTO erro = new ErroDTO(
                Instant.now(),
                403,
                "Você não possui permissão para acessar este recurso.",
                request.getRequestURI()
        );

        // Serializa o objeto de erro para JSON e envia como resposta
        String json = new ObjectMapper().writeValueAsString(erro);
        response.getWriter().write(json);
    }
}
