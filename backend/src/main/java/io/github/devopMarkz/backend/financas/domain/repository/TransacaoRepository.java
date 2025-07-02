package io.github.devopMarkz.backend.financas.domain.repository;

import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import io.github.devopMarkz.backend.financas.domain.model.Transacao;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("""
        SELECT t FROM Transacao t
        WHERE t.usuario.id = :usuarioId
          AND (:descricao IS NULL OR :descricao = '' OR LOWER(t.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')))
          AND (:categoriaId IS NULL OR t.categoria.id = :categoriaId)
          AND (:tipo IS NULL OR :tipo = '' OR t.tipo = :tipo)
          AND (:valorMin IS NULL OR t.valor >= :valorMin)
          AND (:paga IS NULL OR t.paga = :paga)
          AND (COALESCE(:dataMin, t.dataTransacao) = t.dataTransacao OR t.dataTransacao >= :dataMin)
          AND (COALESCE(:dataMax, t.dataTransacao) = t.dataTransacao OR t.dataTransacao <= :dataMax)
        ORDER BY t.dataTransacao DESC
    """)
    Page<Transacao> filtrarTransacoes(
            @Param("usuarioId") Long usuarioId,
            @Param("descricao") String descricao,
            @Param("categoriaId") Long categoriaId,
            @Param("tipo") Tipo tipo,
            @Param("valorMin") BigDecimal valorMin,
            @Param("paga") Boolean paga,
            @Param("dataMin") LocalDate dataMin,
            @Param("dataMax") LocalDate dataMax,
            Pageable pageable
    );

    @Query(
            "SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t " +
            "WHERE t.usuario.id = :usuarioId " +
                    "AND t.tipo = :tipo " +
                    "AND t.dataTransacao BETWEEN :inicio AND :fim"
    )
    BigDecimal somarPorTipoEPeriodo(@Param("tipo") Tipo tipo,
                                    @Param("inicio") LocalDate dataInicio,
                                    @Param("fim") LocalDate dataFim,
                                    @Param("usuarioId") Long usuarioId);

    boolean existsByCategoria_Id(Long categoriaId);

}
