package io.github.devopMarkz.backend.financas.application.mappers;

import io.github.devopMarkz.backend.financas.application.dto.transacao.TransacaoRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.transacao.TransacaoResponseDTO;
import io.github.devopMarkz.backend.financas.domain.model.Categoria;
import io.github.devopMarkz.backend.financas.domain.model.Transacao;
import io.github.devopMarkz.backend.financas.domain.repository.CategoriaRepository;
import io.github.devopMarkz.backend.financas.infraestrutucture.exception.EntidadeInexistenteException;
import io.github.devopMarkz.backend.shared.config.UsuarioAutenticadoService;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = CategoriaMapper.class)
public abstract class TransacaoMapper {

    @Autowired
    protected CategoriaRepository categoriaRepository;

    @Mapping(target = "categoria", expression = "java(toCategoria(requestDTO))")
    public abstract Transacao toTransacao(TransacaoRequestDTO requestDTO);

    @Mapping(target = "categoria", expression = "java(toCategoria(requestDTO))")
    @Mapping(target = "usuario", expression = "java( toUsuario() )")
    public abstract Transacao toTransacaoUpdated(TransacaoRequestDTO requestDTO);

    @Mapping(target = "valor", expression = "java( toDouble(transacao) )")
    public abstract TransacaoResponseDTO toTransacaoResponseDTO(Transacao transacao);

    protected Categoria toCategoria(TransacaoRequestDTO requestDTO) {
        return categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new EntidadeInexistenteException("Categoria Inexistente!"));
    }

    protected Usuario toUsuario(){
        return UsuarioAutenticadoService.obterUsuario();
    }

    protected Double toDouble(Transacao transacao) {
        return transacao.getValor().doubleValue();
    }

}
