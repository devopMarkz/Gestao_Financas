package io.github.devopMarkz.backend.financas.domain.repository;

import io.github.devopMarkz.backend.financas.domain.model.Categoria;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("""
        SELECT c FROM Categoria c
        WHERE c.usuario.id = :usuarioId
        AND (:nome IS NULL OR :nome = '' OR c.nome = :nome)
        AND (:tipo IS NULL OR :tipo = '' OR c.tipo = :tipo)
        AND (:ativa IS NULL OR c.ativa = :ativa)
        ORDER BY c.nome ASC
    """)
    Page<Categoria> buscarCategoriasFiltradas(
            @Param("usuarioId") Long usuarioId,
            @Param("nome") String nome,
            @Param("tipo") Tipo tipo,
            @Param("ativa") Boolean ativa,
            Pageable pageable
    );

}
