package io.github.devopMarkz.backend.usuario.application.dto.autenticacao;

public class TokenDTO {

    private String acess_token;
    private String refresh_token;

    public TokenDTO() {
    }

    public TokenDTO(String acess_token) {
        this.acess_token = acess_token;
    }

    public TokenDTO(String acess_token, String refresh_token) {
        this.acess_token = acess_token;
        this.refresh_token = refresh_token;
    }

    public String getAcess_token() {
        return acess_token;
    }

    public void setAcess_token(String acess_token) {
        this.acess_token = acess_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
