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

@Service
public class DashboardService {

    private final TransacaoRepository transacaoRepository;

    public DashboardService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public DashboardResumoDTO getResumo(LocalDate dataInicio, LocalDate dataFim) {
        Usuario usuarioLogado = UsuarioAutenticadoService.obterUsuario();

        LocalDate hoje = LocalDate.now();

        if(dataInicio == null){
            dataInicio = hoje.withDayOfMonth(1);
        }

        if(dataFim == null){
            dataFim = hoje.withDayOfMonth(hoje.lengthOfMonth());
        }

        BigDecimal totalReceitas = transacaoRepository.somarPorTipoEPeriodo(Tipo.RECEITA, dataInicio, dataFim, usuarioLogado.getId());
        BigDecimal totalDespesas = transacaoRepository.somarPorTipoEPeriodo(Tipo.DESPESA, dataInicio, dataFim, usuarioLogado.getId());

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new DashboardResumoDTO(totalReceitas, totalDespesas, saldo);
    }
}
