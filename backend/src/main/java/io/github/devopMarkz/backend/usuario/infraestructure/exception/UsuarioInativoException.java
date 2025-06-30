package io.github.devopMarkz.backend.usuario.infraestructure.exception;

import org.springframework.security.core.AuthenticationException;

public class UsuarioInativoException extends AuthenticationException {
    public UsuarioInativoException(String mensagem) {
        super(mensagem);
    }
}