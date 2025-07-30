package io.github.devopMarkz.backend.financas.interfaces.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaRequestDTO;
import io.github.devopMarkz.backend.financas.domain.model.Categoria;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import io.github.devopMarkz.backend.financas.domain.model.Transacao;
import io.github.devopMarkz.backend.financas.domain.repository.CategoriaRepository;
import io.github.devopMarkz.backend.financas.domain.repository.TransacaoRepository;
import io.github.devopMarkz.backend.usuario.domain.model.Perfil;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import io.github.devopMarkz.backend.usuario.domain.repository.UsuarioRepository;
import io.github.devopMarkz.backend.usuario.infraestructure.security.TokenService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoriaControllerTest {

    @LocalServerPort
    private int port;

    private Categoria categoria;

    private CategoriaRequestDTO categoriaRequestDTO;

    private Usuario usuario;

    private String token;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository UsuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        transacaoRepository.deleteAll();
        categoriaRepository.deleteAll();
        usuarioRepository.deleteAll();

        RestAssured.port = port;
        RestAssured.basePath = "";

        categoriaRequestDTO = new CategoriaRequestDTO();
        categoriaRequestDTO.setNome("Categoria Teste");
        categoriaRequestDTO.setTipo(Tipo.RECEITA);

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

    @Nested
    class SalvarCategoria {
        @Test
        void deveSalvarCategoria() throws JsonProcessingException {
            RestAssured.given()
                        .contentType("application/json")
                        .accept(ContentType.JSON)
                        .header("Authorization", "Bearer " + getToken(usuario))
                        .body(categoriaRequestDTO)
                    .when()
                        .post("/categorias")
                    .then()
                        .statusCode(201);
        }
    }

    @Nested
    class EditarCategoria {
        @Test
        void deveAtualizarCategoria() {
            RestAssured.given()
                        .contentType("application/json")
                        .accept(ContentType.JSON)
                        .header("Authorization", "Bearer " + getToken(usuario))
                        .body(categoriaRequestDTO)
                    .when()
                        .put("/categorias/{id}", categoria.getId())
                    .then()
                        .statusCode(204);
        }

        @Test
        void deveRetornar404_QuandoCategoriaNaoEncontrada() {
            RestAssured.given()
                        .contentType("application/json")
                        .accept(ContentType.JSON)
                        .header("Authorization", "Bearer " + getToken(usuario))
                        .body(categoriaRequestDTO)
                    .when()
                        .put("/categorias/{id}", Long.MAX_VALUE)
                    .then()
                        .statusCode(404);
        }

        @Test
        void deveRetornar409_QuandoCategoriaAtualizadaNaoTiverSidoCriadaPeloUsuarioLogado() {
            Usuario newUsuario = new Usuario();
            newUsuario.setNome("Usuario Falso");
            newUsuario.setEmail("usuario_falso@email.com");
            newUsuario.setSenha(passwordEncoder.encode("123456"));
            newUsuario.setPerfil(Perfil.ROLE_ADMIN);
            newUsuario.setAtivo(true);
            newUsuario = usuarioRepository.saveAndFlush(newUsuario);

            RestAssured.given()
                        .contentType("application/json")
                        .accept(ContentType.JSON)
                        .header("Authorization", "Bearer " + getToken(newUsuario))
                        .body(categoriaRequestDTO)
                    .when()
                        .put("/categorias/{id}", categoria.getId())
                    .then()
                        .statusCode(409);
        }
    }

    @Nested
    class DeletarCategoria {
        @Test
        void deveExcluirCategoria() {
            RestAssured.given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                    .delete("categorias/{id}", categoria.getId())
                    .then()
                    .statusCode(204);
        }

        @Test
        void deveRetornarStatus409_QuandoCategoriaNaoFoiCriadaPeloUsuarioLogado() {
            Usuario newUsuario = new Usuario();
            newUsuario.setNome("Usuario Falso");
            newUsuario.setEmail("usuario_falso@email.com");
            newUsuario.setSenha(passwordEncoder.encode("123456"));
            newUsuario.setPerfil(Perfil.ROLE_ADMIN);
            newUsuario.setAtivo(true);
            newUsuario = usuarioRepository.saveAndFlush(newUsuario);

            RestAssured.given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + getToken(newUsuario))
                    .when()
                    .delete("categorias/{id}", categoria.getId())
                    .then()
                    .statusCode(409);
        }

        @Test
        void deveRetornarStatus404_QuandoCategoriaNaoEncontrada() {
            RestAssured.given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                    .delete("categorias/{id}", Long.MAX_VALUE)
                    .then()
                    .statusCode(404);
        }

        @Test
        void deveRetornarStatus409_QuandoCategoriaEstiverAssociadaATransacao() {
            Transacao transacao = new Transacao();
            transacao.setCategoria(categoria);
            transacao.setUsuario(usuario);
            transacao.setDescricao("Descrição genérica");
            transacao.setValor(BigDecimal.valueOf(10.0));
            transacao.setDataTransacao(LocalDate.now());
            transacao.setTipo(Tipo.RECEITA);
            transacao.setObservacoes("Observação de receita");

            transacao = transacaoRepository.saveAndFlush(transacao);

            RestAssured.given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                    .delete("categorias/{id}", categoria.getId())
                    .then()
                    .statusCode(409);

            transacaoRepository.delete(transacao);
        }
    }

    private String getToken(Usuario usuario) {
        return tokenService.obterToken(usuario);
    }

}