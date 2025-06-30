package io.github.devopMarkz.backend.usuario.application.dto.usuarios;

import io.github.devopMarkz.backend.usuario.domain.model.Perfil;

public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;
    private Boolean ativo;

    public UsuarioDTO() {}

    public UsuarioDTO(Long id, String nome, String email, Perfil perfil, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.ativo = ativo;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
