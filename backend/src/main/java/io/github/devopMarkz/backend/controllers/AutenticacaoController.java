package io.github.devopMarkz.backend.controllers;

import io.github.devopMarkz.backend.dto.LoginDTO;
import io.github.devopMarkz.backend.dto.TokenDTO;
import io.github.devopMarkz.backend.model.Usuario;
import io.github.devopMarkz.backend.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> autenticar(@RequestBody LoginDTO loginDTO) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha());

        var authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String token = tokenService.obterToken(usuario);

        return ResponseEntity.ok(new TokenDTO(token));
    }

}
