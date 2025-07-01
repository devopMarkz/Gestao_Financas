package io.github.devopMarkz.backend.financas.application.service;

import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaResponseDTO;
import io.github.devopMarkz.backend.financas.domain.model.Categoria;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import io.github.devopMarkz.backend.financas.domain.repository.CategoriaRepository;
import io.github.devopMarkz.backend.financas.infraestrutucture.exception.CategoriaInexistenteException;
import io.github.devopMarkz.backend.financas.infraestrutucture.exception.OperacaoInvalidaException;
import io.github.devopMarkz.backend.shared.config.UsuarioAutenticadoService;
import io.github.devopMarkz.backend.shared.utils.StringPadronization;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;
    private final StringPadronization stringPadronization;
    private final HttpServletRequest request;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            ModelMapper modelMapper,
                            HttpServletRequest request,
                            StringPadronization stringPadronization) {
        this.categoriaRepository = categoriaRepository;
        this.modelMapper = modelMapper;
        this.request = request;
        this.stringPadronization = stringPadronization;
    }

    @Transactional
    public CategoriaResponseDTO save(CategoriaRequestDTO categoriaRequestDTO) {
        Categoria categoria = modelMapper.map(categoriaRequestDTO, Categoria.class);

        categoria.setNome(normalizarNome(categoria.getNome()));

        Usuario usuarioLogado = obterUsuarioLogado();

        categoria.setUsuario(usuarioLogado);

        categoria = categoriaRepository.save(categoria);

        return modelMapper.map(categoria, CategoriaResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> findCategoriasByFilter(String nome, Tipo tipo, Boolean ativa, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Usuario usuarioLogado = obterUsuarioLogado();

        Page<Categoria> categorias = categoriaRepository.buscarCategoriasFiltradas(usuarioLogado.getId(), nome, tipo, ativa, pageable);

        return categorias
                .map(cat -> modelMapper.map(cat, CategoriaResponseDTO.class));
    }

    @Transactional
    public void update(Long id, CategoriaRequestDTO categoriaRequestDTO) {
        Categoria categoria = categoriaRepository.findById(id).
                orElseThrow(() -> new CategoriaInexistenteException("Categoria inexistente!"));

        verificaSeCategoriaFoiCriadaPeloUsuarioLogado(categoria);

        categoria.setNome(categoriaRequestDTO.getNome() == null ? categoria.getNome() : normalizarNome(categoriaRequestDTO.getNome()));
        categoria.setTipo(categoriaRequestDTO.getTipo() == null ? categoria.getTipo() : categoriaRequestDTO.getTipo());

        categoriaRepository.save(categoria);
    }

    @Transactional
    public void delete(Long id) {
        Categoria categoria = categoriaRepository.getReferenceById(id);

        verificaSeCategoriaFoiCriadaPeloUsuarioLogado(categoria);

        if(categoria == null) {
            throw new CategoriaInexistenteException("Categoria inexistente!");
        }

        categoriaRepository.deleteById(id);
    }

    private Usuario obterUsuarioLogado() {
        return UsuarioAutenticadoService.obterUsuario();
    }

    private void verificaSeCategoriaFoiCriadaPeloUsuarioLogado(Categoria categoria) {
        String method = request.getMethod();

        Usuario usuarioLogado = obterUsuarioLogado();

        if(!categoria.getUsuario().equals(usuarioLogado)) {
            throw new OperacaoInvalidaException("Operação " + method + " inviável. A categoria " + categoria.getNome() + " não pertence ao usuário " + usuarioLogado.getEmail());
        }
    }

    private String normalizarNome(String nome) {
        return stringPadronization.converter(nome);
    }

}
