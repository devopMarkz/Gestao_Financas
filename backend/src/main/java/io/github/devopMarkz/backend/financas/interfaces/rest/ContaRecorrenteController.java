package io.github.devopMarkz.backend.financas.interfaces.rest;

import io.github.devopMarkz.backend.financas.application.dto.conta_recorrente.ContaRecorrenteRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.conta_recorrente.ContaRecorrenteResponseDTO;
import io.github.devopMarkz.backend.financas.application.service.ContaRecorrenteService;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static io.github.devopMarkz.backend.shared.utils.UriGenerator.generateUri;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST Controller para gerenciamento das Contas Recorrentes.

 * Fornece endpoints para CRUD (Criar, Ler, Atualizar, Deletar) e criação de transações
 * relacionadas às contas recorrentes.

 * Os métodos possuem controle de acesso baseado em papéis (ROLE_USER e ROLE_ADMIN).
 *
 * @author devopMarkz
 */
@RestController
@RequestMapping("/contas-recorrentes")
@Tag(name = "Contas Recorrentes", description = "API para gerenciar contas financeiras recorrentes")
public class ContaRecorrenteController {

    private final ContaRecorrenteService contaRecorrenteService;

    public ContaRecorrenteController(ContaRecorrenteService contaRecorrenteService) {
        this.contaRecorrenteService = contaRecorrenteService;
    }

    /**
     * Lista contas recorrentes com filtros opcionais e paginação.
     *
     * @param descricao filtro pela descrição da conta (opcional)
     * @param categoriaId filtro pelo id da categoria (opcional)
     * @param tipo filtro pelo tipo da conta (opcional)
     * @param ativa filtro por status da conta (ativa ou inativa) (opcional)
     * @param diaVencimento filtro pelo dia de vencimento da conta (opcional)
     * @param pageable paginação e ordenação dos resultados
     * @return página de contas recorrentes filtradas
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Listar contas recorrentes",
            description = "Retorna uma página de contas recorrentes filtradas por parâmetros opcionais",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaRecorrenteResponseDTO.class)))
            })
    public ResponseEntity<Page<ContaRecorrenteResponseDTO>> listarContasRecorrentes(
            @Parameter(description = "Descrição parcial ou completa para filtro") @RequestParam(required = false) String descricao,
            @Parameter(description = "ID da categoria para filtro") @RequestParam(required = false) Long categoriaId,
            @Parameter(description = "Tipo da conta para filtro") @RequestParam(required = false) Tipo tipo,
            @Parameter(description = "Status ativo/inativo para filtro") @RequestParam(required = false) Boolean ativa,
            @Parameter(description = "Dia de vencimento para filtro") @RequestParam(required = false) Short diaVencimento,
            @PageableDefault(page = 0, size = 30, sort = "diaVencimento", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<ContaRecorrenteResponseDTO> contas = contaRecorrenteService.buscarContasFiltradas(
                descricao, categoriaId, tipo, ativa, diaVencimento, pageable
        );
        return ResponseEntity.ok(contas);
    }

    /**
     * Cria uma nova conta recorrente.
     *
     * @param dto dados da conta recorrente para criação
     * @return dados da conta criada com URI no header Location
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Criar conta recorrente",
            description = "Cria uma nova conta recorrente e retorna os dados da conta criada",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Conta criada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaRecorrenteResponseDTO.class)))
            })
    public ResponseEntity<ContaRecorrenteResponseDTO> criarContaRecorrente(
            @Valid @RequestBody ContaRecorrenteRequestDTO dto
    ) {
        ContaRecorrenteResponseDTO response = contaRecorrenteService.salvar(dto);
        URI location = generateUri(response.getId());
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Busca uma conta recorrente pelo seu ID.
     *
     * @param id identificador da conta recorrente
     * @return dados da conta encontrada
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Buscar conta por ID",
            description = "Retorna os dados da conta recorrente identificada pelo ID informado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaRecorrenteResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
            })
    public ResponseEntity<ContaRecorrenteResponseDTO> buscarPorId(@PathVariable Long id) {
        ContaRecorrenteResponseDTO dto = contaRecorrenteService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Atualiza os dados de uma conta recorrente existente.
     *
     * @param id identificador da conta a ser atualizada
     * @param dto dados atualizados da conta recorrente
     * @return dados da conta atualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Atualizar conta recorrente",
            description = "Atualiza os dados da conta recorrente com o ID informado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaRecorrenteResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
            })
    public ResponseEntity<ContaRecorrenteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContaRecorrenteRequestDTO dto
    ) {
        ContaRecorrenteResponseDTO atualizado = contaRecorrenteService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * Deleta uma conta recorrente pelo seu ID.
     *
     * @param id identificador da conta a ser removida
     * @return resposta sem conteúdo (204) em caso de sucesso
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Deletar conta recorrente",
            description = "Remove a conta recorrente com o ID informado",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Conta deletada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
            })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contaRecorrenteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cria uma transação a partir de uma conta recorrente.
     *
     * @param idConta identificador da conta recorrente que terá a transação criada
     * @param request objeto HttpServletRequest para construir URI da nova transação
     * @return resposta com status 201 Created e URI da transação criada
     */
    @PostMapping("/{idConta}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Criar transação para conta recorrente",
            description = "Gera uma nova transação a partir da conta recorrente identificada pelo ID",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Transação criada com sucesso")
            })
    public ResponseEntity<Void> criarTransacao(@PathVariable Long idConta, HttpServletRequest request) {
        Long id = contaRecorrenteService.criarTransacao(idConta);
        return ResponseEntity.created(URI.create(request.getContextPath() + "/transacoes/" + id)).build();
    }

}
