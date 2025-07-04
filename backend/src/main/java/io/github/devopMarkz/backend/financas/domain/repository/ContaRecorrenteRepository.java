package io.github.devopMarkz.backend.financas.domain.repository;

import io.github.devopMarkz.backend.financas.domain.model.ContaRecorrente;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContaRecorrenteRepository extends JpaRepository<ContaRecorrente, Long> {

    @Query("SELECT obj FROM ContaRecorrente obj " +
            "WHERE obj.usuario.id = :usuarioId " +
            "AND obj.id = :id")
    Optional<ContaRecorrente> buscarPorId(
            @Param("usuarioId") Long usuarioId,
            @Param("id") Long id
    );

    @Query("""
        SELECT c FROM ContaRecorrente c
        WHERE c.usuario.id = :usuarioId
        AND (:descricao IS NULL OR :descricao = '' OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')))
        AND (:categoriaId IS NULL OR c.categoria.id = :categoriaId)
        AND (:tipo IS NULL OR c.tipo = :tipo)
        AND (:ativa IS NULL OR c.ativa = :ativa)
        AND (:diaVencimento IS NULL OR c.diaVencimento = :diaVencimento)
        ORDER BY c.diaVencimento ASC
    """)
    Page<ContaRecorrente> buscarComFiltros(
            @Param("usuarioId") Long usuarioId,
            @Param("descricao") String descricao,
            @Param("categoriaId") Long categoriaId,
            @Param("tipo") Tipo tipo,
            @Param("ativa") Boolean ativa,
            @Param("diaVencimento") Short diaVencimento,
            Pageable pageable
    );

}
