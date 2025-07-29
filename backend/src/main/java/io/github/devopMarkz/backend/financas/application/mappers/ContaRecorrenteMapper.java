package io.github.devopMarkz.backend.financas.application.mappers;

import io.github.devopMarkz.backend.financas.application.dto.conta_recorrente.ContaRecorrenteRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.conta_recorrente.ContaRecorrenteResponseDTO;
import io.github.devopMarkz.backend.financas.domain.model.Categoria;
import io.github.devopMarkz.backend.financas.domain.model.ContaRecorrente;
import io.github.devopMarkz.backend.financas.domain.repository.CategoriaRepository;
import io.github.devopMarkz.backend.financas.infraestrutucture.exception.EntidadeInexistenteException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ContaRecorrenteMapper {

    private CategoriaRepository categoriaRepository;

    @Mapping(target = "categoria", expression = "java( toCategoria(requestDTO) )")
    public abstract ContaRecorrente toContaRecorrente(ContaRecorrenteRequestDTO requestDTO);

    @Mapping(target = "categoriaId", expression = "java( toCategoriaId(contaRecorrente) )")
    @Mapping(target = "categoriaNome", expression = "java( toCategoriaNome(contaRecorrente) )")
    @Mapping(target = "tipo", expression = "java( toTipoString(contaRecorrente) )")
    public abstract ContaRecorrenteResponseDTO toContaRecorrenteResponseDTO(ContaRecorrente contaRecorrente);

    protected Categoria toCategoria(ContaRecorrenteRequestDTO requestDTO) {
        return categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new EntidadeInexistenteException("Categoria inexistente!"));
    }

    protected Long toCategoriaId(ContaRecorrente contaRecorrente) {
        return contaRecorrente.getId();
    }

    protected String toCategoriaNome(ContaRecorrente contaRecorrente) {
        return contaRecorrente.getCategoria().getNome();
    }

    protected String toTipoString(ContaRecorrente contaRecorrente) {
        return contaRecorrente.getTipo().toString();
    }

}
