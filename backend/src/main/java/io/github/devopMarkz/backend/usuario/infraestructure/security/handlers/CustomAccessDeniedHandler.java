package io.github.devopMarkz.backend.usuario.infraestructure.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.devopMarkz.backend.shared.dto.ErroDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErroDTO erro = new ErroDTO(Instant.now(), 403, "Você não possui permissão para acessar este recurso.", request.getRequestURI());

        String json = new ObjectMapper().writeValueAsString(erro);

        response.getWriter().write(json);
    }
}

