package io.github.devopMarkz.backend.financas.interfaces.rest;

import io.github.devopMarkz.backend.financas.domain.model.Categoria;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import io.github.devopMarkz.backend.financas.domain.repository.CategoriaRepository;
import io.github.devopMarkz.backend.usuario.domain.model.Perfil;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import io.github.devopMarkz.backend.usuario.domain.repository.UsuarioRepository;
import io.github.devopMarkz.backend.usuario.infraestructure.security.TokenService;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoriaControllerTest {

    @LocalServerPort
    private int port;

    private Categoria categoria;

    private Usuario usuario;

    private String token;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository UsuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        categoriaRepository.deleteAll(); // sempre delete dependentes primeiro
        usuarioRepository.deleteAll();

        RestAssured.port = port;
        RestAssured.basePath = "";

        usuario = new Usuario();
        usuario.setNome("Usuario");
        usuario.setEmail("usuario@email.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        usuario.setPerfil(Perfil.ROLE_ADMIN);
        usuario.setAtivo(true);
        usuario = usuarioRepository.save( usuario);

        categoria = new Categoria();
        categoria.setNome("Categoria 1");
        categoria.setUsuario(usuario);
        categoria.setTipo(Tipo.RECEITA);
        categoria = categoriaRepository.save(categoria);
    }

    @Nested
    class ObterCategorias {
        @Test
        void deveRetornarListaDeCategorias() {
            RestAssured.given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                    .get("/categorias")
                    .then()
                    .statusCode(200)
                    .body("content.size()", Matchers.is(1))
                    .body("content[0].nome", Matchers.equalTo("Categoria 1"))
                    .body("content[0].tipo", Matchers.equalTo("RECEITA"))
                    .body("content[0].ativa", Matchers.equalTo(true));
        }
    }

    @Nested
    class ObterCategoriaPorId {
        @Test
        void deveRetornarCategoriaPorId() {
            RestAssured.given()
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                        .get("/categorias/{id}", categoria.getId())
                    .then()
                        .statusCode(200)
                        .body("nome", Matchers.equalTo("Categoria 1"))
                        .body("tipo", Matchers.equalTo("RECEITA"));
        }

        @Test
        void deveRetornarErro404_QuandoCategoriaNaoEncontrada() {
            RestAssured.given()
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                        .get("/categorias/{id}", Long.MAX_VALUE)
                    .then()
                        .statusCode(404);
        }
    }

    private String getToken(Usuario usuario) {
        return tokenService.obterToken(usuario);
    }

}