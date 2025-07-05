package io.github.devopMarkz.backend.financas.interfaces.rest;

import io.github.devopMarkz.backend.financas.application.dto.transacao.TransacaoRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.transacao.TransacaoResponseDTO;
import io.github.devopMarkz.backend.financas.application.service.TransacaoService;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

import static io.github.devopMarkz.backend.shared.utils.UriGenerator.generateUri;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controller REST para gerenciamento de transações financeiras.

 * Fornece endpoints para criação, leitura, atualização, exclusão e listagem filtrada de transações.
 * O acesso é restrito a usuários com os papéis ROLE_USER e ROLE_ADMIN.
 *
 * @author devopMarkz
 */
@RestController
@RequestMapping("/transacoes")
@Tag(name = "Transações", description = "API para gerenciar transações financeiras")
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    /**
     * Cria uma nova transação financeira.
     *
     * @param dto dados da transação para criação
     * @return dados da transação criada com URI no header Location
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Criar nova transação",
            description = "Cria uma nova transação financeira e retorna os dados da transação criada",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Transação criada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransacaoResponseDTO.class)))
            })
    public ResponseEntity<TransacaoResponseDTO> criar(@Valid @RequestBody TransacaoRequestDTO dto) {
        TransacaoResponseDTO resposta = service.salvar(dto);
        URI location = generateUri(resposta.getId());
        return ResponseEntity.created(location).body(resposta);
    }

    /**
     * Lista transações financeiras com filtros opcionais e paginação simples.
     *
     * @param descricao filtro por descrição (opcional)
     * @param categoriaId filtro por id da categoria (opcional)
     * @param tipo filtro por tipo da transação (opcional)
     * @param valorMin filtro por valor mínimo (opcional)
     * @param paga filtro pelo status de pagamento (opcional)
     * @param dataMin filtro por data mínima (opcional)
     * @param dataMax filtro por data máxima (opcional)
     * @param pageNumber número da página para paginação (padrão 0)
     * @param pageSize tamanho da página para paginação (padrão 20)
     * @return página de transações filtradas
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Listar transações com filtros",
            description = "Retorna uma página de transações financeiras filtradas por diversos parâmetros",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransacaoResponseDTO.class)))
            })
    public ResponseEntity<Page<TransacaoResponseDTO>> listarComFiltro(
            @Parameter(description = "Descrição para filtro (opcional)") @RequestParam(required = false) String descricao,
            @Parameter(description = "ID da categoria para filtro (opcional)") @RequestParam(required = false) Long categoriaId,
            @Parameter(description = "Tipo da transação para filtro (opcional)") @RequestParam(required = false) Tipo tipo,
            @Parameter(description = "Valor mínimo para filtro (opcional)") @RequestParam(required = false) BigDecimal valorMin,
            @Parameter(description = "Status de pagamento para filtro (opcional)") @RequestParam(required = false) Boolean paga,
            @Parameter(description = "Data mínima para filtro (opcional)") @RequestParam(required = false) LocalDate dataMin,
            @Parameter(description = "Data máxima para filtro (opcional)") @RequestParam(required = false) LocalDate dataMax,
            @Parameter(description = "Número da página para paginação (default 0)") @RequestParam(defaultValue = "0") int pageNumber,
            @Parameter(description = "Tamanho da página para paginação (default 20)") @RequestParam(defaultValue = "20") int pageSize
    ) {
        Page<TransacaoResponseDTO> resultado = service.findCatgoriasByFilter(
                descricao, categoriaId, tipo, valorMin, paga, dataMin, dataMax, pageNumber, pageSize
        );
        return ResponseEntity.ok(resultado);
    }

    /**
     * Busca uma transação pelo seu ID.
     *
     * @param id identificador da transação
     * @return dados da transação encontrada
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Buscar transação por ID",
            description = "Retorna os dados da transação identificada pelo ID informado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transação encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransacaoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Transação não encontrada", content = @Content)
            })
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        TransacaoResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Atualiza uma transação existente.
     *
     * @param id identificador da transação a ser atualizada
     * @param dto dados atualizados da transação
     * @return dados da transação atualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Atualizar transação",
            description = "Atualiza os dados da transação identificada pelo ID informado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransacaoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Transação não encontrada", content = @Content)
            })
    public ResponseEntity<TransacaoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TransacaoRequestDTO dto) {
        TransacaoResponseDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * Deleta uma transação pelo seu ID.
     *
     * @param id identificador da transação a ser removida
     * @return resposta sem conteúdo (204) em caso de sucesso
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Deletar transação",
            description = "Remove a transação financeira identificada pelo ID informado",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Transação deletada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Transação não encontrada", content = @Content)
            })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
