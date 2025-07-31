package io.github.devopMarkz.backend.financas.interfaces.rest;

import io.github.devopMarkz.backend.financas.application.dto.transacao.TransacaoRequestDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransacaoControllerTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private TransacaoRequestDTO transacaoRequestDTO;

    private TransacaoRequestDTO transacaoRequestDTOPut;

    private Transacao transacao;

    private Categoria categoria;

    private Usuario usuario;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        transacaoRepository.deleteAll();
        categoriaRepository.deleteAll();
        usuarioRepository.deleteAll();

        RestAssured.port = port;
        RestAssured.basePath = "";

        // Usuário
        usuario = new Usuario();
        usuario.setNome("Usuario");
        usuario.setEmail("usuario@email.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        usuario.setPerfil(Perfil.ROLE_ADMIN);
        usuario.setAtivo(true);
        usuario = usuarioRepository.save( usuario);

        // Categoria
        categoria = new Categoria();
        categoria.setNome("Categoria 1");
        categoria.setUsuario(usuario);
        categoria.setTipo(Tipo.RECEITA);
        categoria = categoriaRepository.save(categoria);

        // Transação
        transacao = new Transacao();
        transacao.setCategoria(categoria);
        transacao.setUsuario(usuario);
        transacao.setDescricao("Salário mensal");
        transacao.setValor(BigDecimal.valueOf(1700.0));
        transacao.setDataTransacao(LocalDate.now());
        transacao.setTipo(Tipo.RECEITA);
        transacao.setObservacoes("Salário Jul/25");
        transacao = transacaoRepository.save(transacao);

        // Transação pra testar o POST
        transacaoRequestDTO = new TransacaoRequestDTO();
        transacaoRequestDTO.setCategoriaId(categoria.getId());
        transacaoRequestDTO.setDescricao("Transação de POST");
        transacaoRequestDTO.setValor(BigDecimal.valueOf(1000.0));
        transacaoRequestDTO.setDataTransacao(LocalDate.now());
        transacaoRequestDTO.setTipo(Tipo.RECEITA);
        transacaoRequestDTO.setObservacoes("Observações de POST");

        // Transação pra testar o PUT
        transacaoRequestDTOPut = new TransacaoRequestDTO();
        transacaoRequestDTOPut.setCategoriaId(categoria.getId());
        transacaoRequestDTOPut.setDescricao("Transação de PUT");
        transacaoRequestDTOPut.setValor(BigDecimal.valueOf(1000.0));
        transacaoRequestDTOPut.setDataTransacao(LocalDate.now());
        transacaoRequestDTOPut.setTipo(Tipo.RECEITA);
        transacaoRequestDTOPut.setObservacoes("Observações de PUT");
    }

    @Nested
    class Criar {
        @Test
        void deveCriarTransacao(){
            RestAssured.given()
                    .contentType("application/json")
                    .accept(ContentType.JSON)
                    .body(transacaoRequestDTO)
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                    .post("/transacoes")
                    .then()
                    .statusCode(201);
        }

        @Test
        void deveRetornarStatus409_QuandoTipoTransacaoForDiferenteDeTipoCategoria(){
            TransacaoRequestDTO newTransacao = new TransacaoRequestDTO();
            newTransacao.setTipo(Tipo.DESPESA);
            newTransacao.setCategoriaId(categoria.getId());
            newTransacao.setDataTransacao(LocalDate.now());
            newTransacao.setDescricao("Transação pra dar erro");
            newTransacao.setValor(BigDecimal.valueOf(1000.0));
            newTransacao.setObservacoes("Observação genérica");

            RestAssured.given()
                    .contentType("application/json")
                    .accept(ContentType.JSON)
                    .body(newTransacao)
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                    .post("/transacoes")
                    .then()
                    .statusCode(409);
        }
    }

    @Nested
    class ListarComFiltro {
        @Test
        void deveRetornarTransacoesComFiltro(){
            RestAssured.given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .queryParam("valor", BigDecimal.valueOf(1700.0))
                    .queryParam("descricao", "Salário mensal")
                    .queryParam("tipo", "RECEITA")
                    .queryParam("paga", "true")
                    .queryParam("categoriaId", categoria.getId())
                    .get("/transacoes")
                    .then()
                    .statusCode(200)
                    .body("content.size()", Matchers.is(1));
        }
    }

    @Nested
    class BuscarPorId {
        @Test
        void deveRetornarTransacao_QuandoBuscarPorId() {
            RestAssured.given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                    .get("/transacoes/{id}", transacao.getId())
                    .then()
                    .statusCode(200)
                    .body("descricao", Matchers.equalTo(transacao.getDescricao()));
        }

        @Test
        void deveRetornarStatus404_QuandoIdNaoEncontrado() {
            RestAssured.given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                    .get("/transacoes/{id}", Long.MAX_VALUE)
                    .then()
                    .statusCode(404);
        }

        @Test
        void deveRetornarStatus409_QuandoTransacaoNaoPertenceAoUsuarioLogado() {
            Usuario newUsuario = new Usuario("Fake", "fake@gmail.com", passwordEncoder.encode("123"), Perfil.ROLE_ADMIN, true);
            newUsuario = usuarioRepository.save(newUsuario);
            RestAssured.given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + getToken(newUsuario))
                    .when()
                    .get("/transacoes/{id}", transacao.getId())
                    .then()
                    .statusCode(409);
        }

    }

    @Nested
    class Atualizar {
        @Test
        void deveAtualizarTransacao() {
            RestAssured.given()
                    .contentType("application/json")
                    .accept(ContentType.JSON)
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .body(transacaoRequestDTOPut)
                    .when()
                    .put("/transacoes/{id}", transacao.getId())
                    .then()
                    .statusCode(200)
                    .body("descricao", Matchers.equalTo("Transação de PUT"));
        }

        @Test
        void deveRetornarStatus404_QuandoTransacaoNaoEncontrada() {
            RestAssured.given()
                    .contentType("application/json")
                    .accept(ContentType.JSON)
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .body(transacaoRequestDTOPut)
                    .when()
                    .put("/transacoes/{id}", Long.MAX_VALUE)
                    .then()
                    .statusCode(404);
        }

        @Test
        void deveRetornarStatus409_QuandoTipoTransacaoForDiferenteDeTipoCategoria(){
            TransacaoRequestDTO newTransacao = new TransacaoRequestDTO();
            newTransacao.setTipo(Tipo.DESPESA);
            newTransacao.setCategoriaId(categoria.getId());
            newTransacao.setDataTransacao(LocalDate.now());
            newTransacao.setDescricao("Transação pra dar erro");
            newTransacao.setValor(BigDecimal.valueOf(1000.0));
            newTransacao.setObservacoes("Observação genérica");

            RestAssured.given()
                    .contentType("application/json")
                    .accept(ContentType.JSON)
                    .body(newTransacao)
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                    .put("/transacoes/{id}", transacao.getId())
                    .then()
                    .statusCode(409);
        }
    }

    @Nested
    class Deletar {
        @Test
        void deveDeletarTransacao() {
            RestAssured.given()
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                    .delete("/transacoes/{id}", transacao.getId())
                    .then()
                    .statusCode(204);
        }

        @Test
        void deveRetornarStatus404_QuandoTransacaoNaoExistente() {
            RestAssured.given()
                    .header("Authorization", "Bearer " + getToken(usuario))
                    .when()
                    .delete("/transacoes/{id}", Long.MAX_VALUE)
                    .then()
                    .statusCode(404);
        }

        @Test
        void deveRetornarStatus409_QuandoTransacaoNaoForCriadaPorUsuarioLogado() {
            Usuario newUsuario = new Usuario("Fake", "fake@gmail.com", passwordEncoder.encode("123"), Perfil.ROLE_ADMIN, true);
            newUsuario = usuarioRepository.save(newUsuario);
            RestAssured.given()
                    .header("Authorization", "Bearer " + getToken(newUsuario))
                    .when()
                    .delete("/transacoes/{id}", transacao.getId())
                    .then()
                    .statusCode(409);
        }
    }

    private String getToken(Usuario usuario) {
        return tokenService.obterToken(usuario);
    }

}