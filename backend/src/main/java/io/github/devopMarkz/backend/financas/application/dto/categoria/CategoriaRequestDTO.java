package io.github.devopMarkz.backend.financas.application.dto.categoria;

import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import jakarta.validation.constraints.NotBlank;

public class CategoriaRequestDTO {

    @NotBlank(message = "Nome não pode ser nulo  ou vazio.")
    private String nome;

    private Tipo tipo;

    public CategoriaRequestDTO() {
    }

    public CategoriaRequestDTO(String nome, Tipo tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
}
