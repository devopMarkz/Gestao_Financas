package io.github.devopMarkz.backend.financas.application.mappers;

import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaResponseDTO;
import io.github.devopMarkz.backend.financas.domain.model.Categoria;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CategoriaMapper {

    public abstract Categoria toCategoria(CategoriaRequestDTO requestDTO);

    @Mapping(target = "tipo", expression = "java(toTipo(responseDTO.getTipo()))")
    public abstract Categoria toCategoria(CategoriaResponseDTO responseDTO);

    public abstract CategoriaResponseDTO toCategoriaResponseDTO(Categoria categoria);

    public abstract CategoriaRequestDTO toCategoriaRequestDTO(Categoria categoria);

    protected Tipo toTipo(String tipo) {
        return Tipo.valueOf(tipo.toUpperCase());
    }

}
