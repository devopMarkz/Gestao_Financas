package io.github.devopMarkz.backend.usuario.interfaces.rest;

import io.github.devopMarkz.backend.usuario.application.dto.usuarios.UsuarioCreateDTO;
import io.github.devopMarkz.backend.usuario.application.dto.usuarios.UsuarioDTO;
import io.github.devopMarkz.backend.usuario.application.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static io.github.devopMarkz.backend.shared.utils.UriGenerator.generateUri;

/**
 * Controlador REST responsável pelas operações relacionadas aos usuários do sistema.
 * Fornece endpoints para criação e listagem de usuários.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Construtor que injeta o serviço de usuários.
     *
     * @param usuarioService serviço de manipulação de usuários.
     */
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint para criação de um novo usuário.
     * Recebe os dados necessários para cadastro, realiza validação e retorna o usuário criado.
     *
     * @param usuarioCreateDTODTO objeto com os dados do novo usuário.
     * @return {@link ResponseEntity} contendo o usuário criado e o header "Location" com a URI do recurso.
     */
    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTODTO) {
        var usuarioDTO = usuarioService.salvarUsuario(usuarioCreateDTODTO);
        URI location = generateUri(usuarioDTO.getId());
        return ResponseEntity.created(location).body(usuarioDTO);
    }

    /**
     * Endpoint para listar todos os usuários cadastrados no sistema.
     * Requer autenticação com perfil de usuário comum (ROLE_USER).
     *
     * @return {@link ResponseEntity} contendo a lista de usuários.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

}
