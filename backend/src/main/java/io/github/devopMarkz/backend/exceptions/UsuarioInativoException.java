package io.github.devopMarkz.backend.exceptions;

public class UsuarioInativoException extends RuntimeException {
    public UsuarioInativoException(String mensagem) {
        super(mensagem);
    }
}