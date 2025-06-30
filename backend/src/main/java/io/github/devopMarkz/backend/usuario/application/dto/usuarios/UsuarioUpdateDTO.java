package io.github.devopMarkz.backend.usuario.application.dto.usuarios;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioUpdateDTO {

    @Size(min = 1, max = 255, message = "Nome pode ter no máximo 255 caracteres.")
    @NotBlank(message = "Nome não pode estar nulo ou vazio.")
    private String nome;

    @Size(min = 1, max = 255, message = "E-mail pode ter no máximo 255 caracteres.")
    @NotBlank(message = "E-mail não pode estar nulo ou vazio.")
    private String email;

    private String senha;

    public UsuarioUpdateDTO() {}

    public UsuarioUpdateDTO(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public UsuarioUpdateDTO(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;

        if(senha == null || senha.isEmpty()){
            this.senha = null;
        }

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
}
