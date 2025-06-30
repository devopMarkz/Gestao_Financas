package io.github.devopMarkz.backend.usuario.infraestructure.exception;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
