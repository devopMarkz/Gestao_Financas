package io.github.devopMarkz.backend.financas.infraestrutucture.exception;

public class EntidadeInexistenteException extends RuntimeException {
    public EntidadeInexistenteException(String message) {
        super(message);
    }
}
