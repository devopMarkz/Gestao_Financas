package io.github.devopMarkz.backend.financas.interfaces.rest;

import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaResponseDTO;
import io.github.devopMarkz.backend.financas.application.service.CategoriaService;
import io.github.devopMarkz.backend.financas.domain.model.TipoCategoria;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static io.github.devopMarkz.backend.shared.utils.UriGenerator.generateUri;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Page<CategoriaResponseDTO>> obterCategorias(
            @RequestParam(name = "nomeCategoria", required = false) String nomeCategoria,
            @RequestParam(name = "tipoCategoria", required = false) TipoCategoria tipoCategoria,
            @RequestParam(name = "ativa", required = false, defaultValue = "true") Boolean ativa,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        Page<CategoriaResponseDTO> categorias = categoriaService.findCategoriasByFilter(nomeCategoria, tipoCategoria, ativa, pageNumber, pageSize);
        return ResponseEntity.ok(categorias);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> salvarCategoria(@RequestBody @Valid CategoriaRequestDTO categoriaRequestDTO) {
        CategoriaResponseDTO categoriaResponse = categoriaService.save(categoriaRequestDTO);
        URI location = generateUri(categoriaResponse.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> editarCategoria(
            @PathVariable("id") Long idCategoria,
            @RequestBody @Valid CategoriaRequestDTO categoriaRequestDTO
    ) {
        categoriaService.update(idCategoria, categoriaRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deletarCategoria(@PathVariable("id") Long idCategoria) {
        categoriaService.delete(idCategoria);
        return ResponseEntity.noContent().build();
    }

}
