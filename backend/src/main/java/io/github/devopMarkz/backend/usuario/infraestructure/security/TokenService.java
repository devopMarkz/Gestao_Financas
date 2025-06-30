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

@Service
public class TokenService {

    @Value("${secret-token}")
    private String secret;

    private final UsuarioRepository usuarioRepository;

    public TokenService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String obterToken(Usuario usuario) {
        return gerarToken(usuario);
    }

    private String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withSubject(usuario.getEmail())
                    .withIssuer("biblioteca-api")
                    .withClaim("roles", List.of(usuario.getPerfil().name()))
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                    .sign(algorithm);
        } catch (JWTCreationException e){
            throw new TokenException("Erro ao gerar token!");
        }
    }

    @Transactional(readOnly = true)
    public Usuario obterUsuario(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String email = JWT.require(algorithm)
                    .withIssuer("biblioteca-api")
                    .build()
                    .verify(token)
                    .getSubject();

            return usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente!"));
        } catch (JWTDecodeException e){
            throw new TokenException("Erro ao obter usuario!");
        }
    }
}
