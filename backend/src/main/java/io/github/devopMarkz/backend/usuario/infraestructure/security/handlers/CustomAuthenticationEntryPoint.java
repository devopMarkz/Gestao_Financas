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

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        Throwable causa = (Throwable) request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        String mensagem = authException.getMessage();

        if (causa instanceof UsuarioInativoException e) {
            mensagem = e.getMessage();
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErroDTO erroDTO = new ErroDTO(Instant.now(), 401, mensagem, request.getRequestURI());

        String json = new ObjectMapper().writeValueAsString(erroDTO);

        response.getWriter().write(json);
    }

}
