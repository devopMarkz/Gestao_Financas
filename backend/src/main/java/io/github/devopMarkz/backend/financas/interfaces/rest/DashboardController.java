package io.github.devopMarkz.backend.financas.interfaces.rest;

import io.github.devopMarkz.backend.financas.application.dto.dashboard.DashboardResumoDTO;
import io.github.devopMarkz.backend.financas.application.service.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controller REST para endpoints do Dashboard financeiro.

 * Oferece resumos financeiros com possibilidade de filtro por período.
 * Controla o acesso para usuários com papel USER e ADMIN.
 *
 * @author devopMarkz
 */
@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "API para resumos financeiros do dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Retorna o resumo financeiro para o dashboard, podendo ser filtrado por intervalo de datas.
     *
     * @param dataInicio (opcional) data inicial para filtro do resumo, no formato ISO (yyyy-MM-dd)
     * @param dataFim (opcional) data final para filtro do resumo, no formato ISO (yyyy-MM-dd)
     * @return resumo financeiro dentro do período informado (ou geral, se não informado)
     */
    @GetMapping("/resumo")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Obter resumo financeiro do dashboard",
            description = "Retorna os principais dados resumidos do financeiro, com filtro opcional por período",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resumo financeiro retornado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DashboardResumoDTO.class)))
            })
    public ResponseEntity<DashboardResumoDTO> getResumo(
            @Parameter(description = "Data inicial do filtro no formato ISO (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final do filtro no formato ISO (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        DashboardResumoDTO dashboard = dashboardService.getResumo(dataInicio, dataFim);
        return ResponseEntity.ok(dashboard);
    }
}
