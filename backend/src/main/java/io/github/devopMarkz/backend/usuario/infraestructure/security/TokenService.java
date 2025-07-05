package io.github.devopMarkz.backend.usuario.infraestructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import io.github.devopMarkz.backend.usuario.infraestructure.exception.TokenException;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import io.github.devopMarkz.backend.usuario.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Serviço responsável pela geração e validação de tokens JWT,
 * bem como pela recuperação do usuário autenticado com base no token.
 */
@Service
public class TokenService {

    @Value("${secret-token}")
    private String secret;

    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor da classe TokenService.
     *
     * @param usuarioRepository Repositório de usuários para buscar dados a partir do e-mail contido no token.
     */
    public TokenService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Gera e retorna um token JWT válido para o usuário fornecido.
     *
     * @param usuario O usuário autenticado.
     * @return Um token JWT como String.
     */
    public String obterToken(Usuario usuario) {
        return gerarToken(usuario);
    }

    /**
     * Gera um token JWT com claims do usuário e expiração de 6 horas.
     *
     * @param usuario O usuário para o qual o token será gerado.
     * @return Token JWT assinado.
     * @throws TokenException se houver falha na criação do token.
     */
    private String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withSubject(usuario.getEmail())
                    .withIssuer("gestao-financeira-api")
                    .withClaim("roles", List.of(usuario.getPerfil().name()))
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(6, ChronoUnit.HOURS))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new TokenException("Erro ao gerar token!");
        }
    }

    /**
     * Decodifica o token JWT e retorna o usuário associado ao e-mail presente no token.
     *
     * @param token O token JWT recebido na requisição.
     * @return O usuário autenticado.
     * @throws TokenException se o token estiver inválido ou se o usuário não for encontrado.
     */
    @Transactional(readOnly = true)
    public Usuario obterUsuario(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String email = JWT.require(algorithm)
                    .withIssuer("gestao-financeira-api")
                    .build()
                    .verify(token)
                    .getSubject();

            return usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente!"));
        } catch (JWTDecodeException e) {
            throw new TokenException("Erro ao obter usuario!");
        }
    }
}
