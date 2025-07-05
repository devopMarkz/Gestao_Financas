package io.github.devopMarkz.backend.financas.application.service;

import io.github.devopMarkz.backend.financas.application.dto.dashboard.DashboardResumoDTO;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import io.github.devopMarkz.backend.financas.domain.repository.TransacaoRepository;
import io.github.devopMarkz.backend.shared.config.UsuarioAutenticadoService;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Serviço responsável por fornecer dados resumidos para o dashboard financeiro do usuário.

 * Realiza consultas agregadas para calcular totais de receitas, despesas e saldo no período informado.
 * Caso os parâmetros de data não sejam fornecidos, utiliza o mês atual como padrão.
 *
 * @author devopMarkz
 */
@Service
public class DashboardService {

    private final TransacaoRepository transacaoRepository;

    public DashboardService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    /**
     * Obtém o resumo financeiro do usuário autenticado no período especificado.

     * Se as datas de início ou fim não forem informadas, utiliza o primeiro e último dia do mês atual, respectivamente.
     *
     * @param dataInicio data inicial do período (pode ser null)
     * @param dataFim data final do período (pode ser null)
     * @return DTO contendo o total de receitas, despesas e o saldo (receitas menos despesas)
     */
    @Transactional
    public DashboardResumoDTO getResumo(LocalDate dataInicio, LocalDate dataFim) {
        Usuario usuarioLogado = UsuarioAutenticadoService.obterUsuario();

        LocalDate hoje = LocalDate.now();

        if (dataInicio == null) {
            dataInicio = hoje.withDayOfMonth(1);
        }

        if (dataFim == null) {
            dataFim = hoje.withDayOfMonth(hoje.lengthOfMonth());
        }

        BigDecimal totalReceitas = transacaoRepository.somarPorTipoEPeriodo(
                Tipo.RECEITA, dataInicio, dataFim, usuarioLogado.getId());

        BigDecimal totalDespesas = transacaoRepository.somarPorTipoEPeriodo(
                Tipo.DESPESA, dataInicio, dataFim, usuarioLogado.getId());

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new DashboardResumoDTO(totalReceitas, totalDespesas, saldo);
    }
}
