package io.github.devopMarkz.backend.usuario.application.dto.usuarios;

import io.github.devopMarkz.backend.usuario.domain.model.Perfil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioCreateDTO {

    @Size(min = 1, max = 255, message = "Nome pode ter no máximo 255 caracteres.")
    @NotBlank(message = "Nome não pode estar nulo ou vazio.")
    private String nome;

    @Size(min = 1, max = 255, message = "E-mail pode ter no máximo 255 caracteres.")
    @NotBlank(message = "E-mail não pode estar nulo ou vazio.")
    private String email;

    @NotBlank(message = "Senha não pode estar nula ou vazia.")
    private String senha;

    private Perfil perfil = Perfil.ROLE_USER;

    private Boolean ativo = true;

    public UsuarioCreateDTO() {}

    public UsuarioCreateDTO(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public UsuarioCreateDTO(String nome, String email, String senha, Perfil perfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public UsuarioCreateDTO(String nome, String email, String senha, Perfil perfil, Boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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
