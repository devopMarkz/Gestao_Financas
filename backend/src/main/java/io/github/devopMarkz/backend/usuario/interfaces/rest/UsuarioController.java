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

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTODTO) {
        var usuarioDTO = usuarioService.salvarUsuario(usuarioCreateDTODTO);
        URI location = generateUri(usuarioDTO.getId());
        return ResponseEntity.created(location).body(usuarioDTO);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

}
