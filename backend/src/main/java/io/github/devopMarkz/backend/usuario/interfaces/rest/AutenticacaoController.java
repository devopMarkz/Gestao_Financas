package io.github.devopMarkz.backend.usuario.interfaces.rest;

import io.github.devopMarkz.backend.usuario.application.dto.autenticacao.LoginDTO;
import io.github.devopMarkz.backend.usuario.application.dto.autenticacao.TokenDTO;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import io.github.devopMarkz.backend.usuario.infraestructure.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável pelo processo de autenticação de usuários.
 *
 * Expõe um endpoint para login que valida as credenciais e gera um token JWT
 * para que o usuário possa acessar as rotas protegidas do sistema.
 */
@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    /**
     * Construtor do controlador de autenticação.
     *
     * @param authenticationManager gerenciador de autenticação do Spring Security.
     * @param tokenService serviço responsável pela geração e leitura de tokens JWT.
     */
    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    /**
     * Endpoint responsável por autenticar um usuário e gerar um token JWT.
     *
     * @param loginDTO DTO contendo o e-mail e a senha fornecidos pelo usuário.
     * @return um {@link ResponseEntity} contendo o token JWT em caso de sucesso.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> autenticar(@RequestBody LoginDTO loginDTO) {
        // Cria o token de autenticação baseado no e-mail e senha fornecidos
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),
                loginDTO.getSenha()
        );

        // Autentica o usuário
        var authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o usuário autenticado
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Gera o token JWT
        String token = tokenService.obterToken(usuario);

        // Retorna o token no corpo da resposta
        return ResponseEntity.ok(new TokenDTO(token));
    }
}
