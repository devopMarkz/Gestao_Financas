package io.github.devopMarkz.backend.financas.application.dto.categoria;

public class CategoriaResponseDTO {

    private Long id;
    private String nome;
    private String tipo;
    private Boolean ativa;

    public CategoriaResponseDTO() {
    }

    public CategoriaResponseDTO(Long id, String nome, String tipo, Boolean ativa) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.ativa = ativa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}
