package io.github.devopMarkz.backend.financas.infraestrutucture.exception;

public class CategoriaInexistenteException extends RuntimeException {
    public CategoriaInexistenteException(String message) {
        super(message);
    }
}
