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

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<TransacaoResponseDTO> criar(@Valid @RequestBody TransacaoRequestDTO dto) {
        TransacaoResponseDTO resposta = service.salvar(dto);
        URI location = generateUri(resposta.getId());
        return ResponseEntity.created(location).body(resposta);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Page<TransacaoResponseDTO>> listarComFiltro(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Tipo tipo,
            @RequestParam(required = false) BigDecimal valorMin,
            @RequestParam(required = false) Boolean paga,
            @RequestParam(required = false) LocalDate dataMin,
            @RequestParam(required = false) LocalDate dataMax,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "20") int pageSize
    ) {
        Page<TransacaoResponseDTO> resultado = service.findCatgoriasByFilter(
                descricao, categoriaId, tipo, valorMin, paga, dataMin, dataMax, pageNumber, pageSize
        );
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        TransacaoResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<TransacaoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TransacaoRequestDTO dto) {
        TransacaoResponseDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
