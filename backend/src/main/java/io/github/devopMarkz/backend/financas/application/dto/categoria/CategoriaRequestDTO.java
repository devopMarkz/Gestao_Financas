package io.github.devopMarkz.backend.financas.application.dto.categoria;

import io.github.devopMarkz.backend.financas.domain.model.TipoCategoria;
import jakarta.validation.constraints.NotBlank;

public class CategoriaRequestDTO {

    @NotBlank(message = "Nome não pode ser nulo  ou vazio.")
    private String nome;

    private TipoCategoria tipo;

    public CategoriaRequestDTO() {
    }

    public CategoriaRequestDTO(String nome, TipoCategoria tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoCategoria getTipo() {
        return tipo;
    }

    public void setTipo(TipoCategoria tipo) {
        this.tipo = tipo;
    }
}
