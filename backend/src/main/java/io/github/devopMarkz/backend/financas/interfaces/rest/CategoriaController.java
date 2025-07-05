package io.github.devopMarkz.backend.financas.interfaces.rest;

import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaResponseDTO;
import io.github.devopMarkz.backend.financas.application.service.CategoriaService;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static io.github.devopMarkz.backend.shared.utils.UriGenerator.generateUri;

/**
 * REST Controller responsável por gerenciar as operações relacionadas às categorias financeiras do usuário.

 * Fornece endpoints para CRUD (Criar, Ler, Atualizar, Deletar) e criação de categorias
 * relacionadas às transações.

 * Os métodos possuem controle de acesso baseado em papéis (ROLE_USER e ROLE_ADMIN).
 *
 * @author devopMarkz
 */
@RestController
@RequestMapping("/categorias")
@Tag(name = "Categoria", description = "Operações relacionadas às categorias financeiras")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /**
     * Busca todas as categorias do usuário autenticado com filtros opcionais.
     *
     * @param nomeCategoria filtro pelo nome da categoria
     * @param tipo tipo da categoria (RECEITA ou DESPESA)
     * @param ativa se a categoria está ativa
     * @param pageNumber número da página
     * @param pageSize tamanho da página
     * @return página de {@link CategoriaResponseDTO}
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Listar categorias", description = "Retorna uma lista paginada de categorias filtradas por nome, tipo e status.")
    public ResponseEntity<Page<CategoriaResponseDTO>> obterCategorias(
            @RequestParam(name = "nomeCategoria", required = false) String nomeCategoria,
            @RequestParam(name = "tipo", required = false) Tipo tipo,
            @RequestParam(name = "ativa", required = false, defaultValue = "true") Boolean ativa,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize
    ) {
        Page<CategoriaResponseDTO> categorias = categoriaService.findCategoriasByFilter(nomeCategoria, tipo, ativa, pageNumber, pageSize);
        return ResponseEntity.ok(categorias);
    }

    /**
     * Busca uma categoria por seu ID.
     *
     * @param id ID da categoria
     * @return {@link CategoriaResponseDTO}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Buscar categoria por ID", description = "Retorna os dados de uma categoria específica.")
    public ResponseEntity<CategoriaResponseDTO> obterCategoriaPorId(
            @Parameter(description = "ID da categoria") @PathVariable Long id
    ) {
        CategoriaResponseDTO categoria = categoriaService.findCategoriaById(id);
        return ResponseEntity.ok(categoria);
    }

    /**
     * Cria uma nova categoria.
     *
     * @param categoriaRequestDTO dados da nova categoria
     * @return status 201 Created com a URI da nova categoria
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Criar nova categoria", description = "Cadastra uma nova categoria.")
    public ResponseEntity<Void> salvarCategoria(
            @RequestBody @Valid CategoriaRequestDTO categoriaRequestDTO
    ) {
        CategoriaResponseDTO categoriaResponse = categoriaService.save(categoriaRequestDTO);
        URI location = generateUri(categoriaResponse.getId());
        return ResponseEntity.created(location).build();
    }

    /**
     * Atualiza os dados de uma categoria existente.
     *
     * @param idCategoria ID da categoria a ser atualizada
     * @param categoriaRequestDTO dados atualizados
     * @return status 204 No Content
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Atualizar categoria", description = "Edita os dados de uma categoria existente.")
    public ResponseEntity<Void> editarCategoria(
            @Parameter(description = "ID da categoria") @PathVariable("id") Long idCategoria,
            @RequestBody @Valid CategoriaRequestDTO categoriaRequestDTO
    ) {
        categoriaService.update(idCategoria, categoriaRequestDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Exclui uma categoria.
     *
     * @param idCategoria ID da categoria a ser excluída
     * @return status 204 No Content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Excluir categoria", description = "Remove uma categoria do sistema.")
    public ResponseEntity<Void> deletarCategoria(
            @Parameter(description = "ID da categoria") @PathVariable("id") Long idCategoria
    ) {
        categoriaService.delete(idCategoria);
        return ResponseEntity.noContent().build();
    }
}
