package io.github.devopMarkz.backend.financas.interfaces.rest;

import io.github.devopMarkz.backend.financas.application.dto.conta_recorrente.ContaRecorrenteRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.conta_recorrente.ContaRecorrenteResponseDTO;
import io.github.devopMarkz.backend.financas.application.service.ContaRecorrenteService;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static io.github.devopMarkz.backend.shared.utils.UriGenerator.generateUri;

@RestController
@RequestMapping("/contas-recorrentes")
public class ContaRecorrenteController {

    private final ContaRecorrenteService contaRecorrenteService;

    public ContaRecorrenteController(ContaRecorrenteService contaRecorrenteService) {
        this.contaRecorrenteService = contaRecorrenteService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Page<ContaRecorrenteResponseDTO>> listarContasRecorrentes(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Tipo tipo,
            @RequestParam(required = false) Boolean ativa,
            @RequestParam(required = false) Short diaVencimento,
            @PageableDefault(page = 0, size = 30, sort = "diaVencimento", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<ContaRecorrenteResponseDTO> contas = contaRecorrenteService.buscarContasFiltradas(
                descricao, categoriaId, tipo, ativa, diaVencimento, pageable
        );
        return ResponseEntity.ok(contas);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ContaRecorrenteResponseDTO> criarContaRecorrente(
            @Valid @RequestBody ContaRecorrenteRequestDTO dto
    ) {
        ContaRecorrenteResponseDTO response = contaRecorrenteService.salvar(dto);
        URI location = generateUri(response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ContaRecorrenteResponseDTO> buscarPorId(@PathVariable Long id) {
        ContaRecorrenteResponseDTO dto = contaRecorrenteService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ContaRecorrenteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContaRecorrenteRequestDTO dto
    ) {
        ContaRecorrenteResponseDTO atualizado = contaRecorrenteService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contaRecorrenteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
