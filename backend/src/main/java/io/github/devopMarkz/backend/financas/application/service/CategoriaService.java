package io.github.devopMarkz.backend.financas.application.service;

import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaResponseDTO;
import io.github.devopMarkz.backend.financas.domain.model.Categoria;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import io.github.devopMarkz.backend.financas.domain.repository.CategoriaRepository;
import io.github.devopMarkz.backend.financas.domain.repository.TransacaoRepository;
import io.github.devopMarkz.backend.financas.infraestrutucture.exception.EntidadeInexistenteException;
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

/**
 * Serviço responsável pela lógica de negócio relacionada às Categorias financeiras.

 * Realiza operações de criação, consulta, atualização e exclusão de categorias,
 * garantindo que as operações sejam feitas apenas pelo usuário que criou a categoria.
 * Também aplica regras de negócio como normalização do nome e validações.
 *
 * @author devopMarkz
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;
    private final StringPadronization stringPadronization;
    private final HttpServletRequest request;
    private final TransacaoRepository transacaoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            ModelMapper modelMapper,
                            HttpServletRequest request,
                            StringPadronization stringPadronization,
                            TransacaoRepository transacaoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.modelMapper = modelMapper;
        this.request = request;
        this.stringPadronization = stringPadronization;
        this.transacaoRepository = transacaoRepository;
    }

    /**
     * Salva uma nova categoria associada ao usuário autenticado,
     * aplicando a normalização do nome.
     *
     * @param categoriaRequestDTO dados da categoria a ser criada
     * @return DTO com os dados da categoria salva
     */
    @Transactional
    public CategoriaResponseDTO save(CategoriaRequestDTO categoriaRequestDTO) {
        Categoria categoria = modelMapper.map(categoriaRequestDTO, Categoria.class);

        categoria.setNome(normalizarNome(categoria.getNome()));

        Usuario usuarioLogado = obterUsuarioLogado();

        categoria.setUsuario(usuarioLogado);

        categoria = categoriaRepository.save(categoria);

        return modelMapper.map(categoria, CategoriaResponseDTO.class);
    }

    /**
     * Busca categorias filtradas por nome, tipo e status,
     * restringindo aos dados do usuário autenticado.
     *
     * @param nome filtro opcional pelo nome da categoria
     * @param tipo filtro opcional pelo tipo da categoria
     * @param ativa filtro opcional pelo status ativo/inativo
     * @param pageNumber número da página para paginação
     * @param pageSize tamanho da página para paginação
     * @return página com categorias filtradas e paginadas
     */
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> findCategoriasByFilter(String nome, Tipo tipo, Boolean ativa, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Usuario usuarioLogado = obterUsuarioLogado();

        Page<Categoria> categorias = categoriaRepository.buscarCategoriasFiltradas(usuarioLogado.getId(), nome, tipo, ativa, pageable);

        return categorias
                .map(cat -> modelMapper.map(cat, CategoriaResponseDTO.class));
    }

    /**
     * Busca uma categoria pelo seu ID.
     *
     * @param id identificador da categoria
     * @return DTO da categoria encontrada
     * @throws EntidadeInexistenteException se a categoria não existir
     * @throws OperacaoInvalidaException se a categoria não pertencer ao usuário autenticado
     */
    @Transactional(readOnly = true)
    public CategoriaResponseDTO findCategoriaById(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntidadeInexistenteException("Categoria inexistente!"));

        verificaSeCategoriaFoiCriadaPeloUsuarioLogado(categoria);

        return modelMapper.map(categoria, CategoriaResponseDTO.class);
    }

    /**
     * Atualiza uma categoria existente pelo ID,
     * permitindo alteração do nome e tipo,
     * com validação de propriedade e normalização do nome.
     *
     * @param id identificador da categoria a ser atualizada
     * @param categoriaRequestDTO dados para atualização da categoria
     * @throws EntidadeInexistenteException se a categoria não existir
     * @throws OperacaoInvalidaException se a categoria não pertencer ao usuário autenticado
     */
    @Transactional
    public void update(Long id, CategoriaRequestDTO categoriaRequestDTO) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntidadeInexistenteException("Categoria inexistente!"));

        verificaSeCategoriaFoiCriadaPeloUsuarioLogado(categoria);

        categoria.setNome(categoriaRequestDTO.getNome() == null ? categoria.getNome() : normalizarNome(categoriaRequestDTO.getNome()));
        categoria.setTipo(categoriaRequestDTO.getTipo() == null ? categoria.getTipo() : categoriaRequestDTO.getTipo());

        categoriaRepository.save(categoria);
    }

    /**
     * Remove uma categoria pelo ID, desde que pertença ao usuário autenticado
     * e não esteja sendo usada em nenhuma transação.
     *
     * @param id identificador da categoria a ser deletada
     * @throws EntidadeInexistenteException se a categoria não existir
     * @throws OperacaoInvalidaException se a categoria não pertencer ao usuário autenticado
     *                                   ou se estiver sendo usada em alguma transação
     */
    @Transactional
    public void delete(Long id) {
        Categoria categoria = categoriaRepository.getReferenceById(id);

        verificaSeCategoriaFoiCriadaPeloUsuarioLogado(categoria);

        if(categoria == null) {
            throw new EntidadeInexistenteException("Categoria inexistente!");
        }

        if(transacaoRepository.existsByCategoria_Id(categoria.getId())) {
            throw new OperacaoInvalidaException("Categoria está sendo usada em uma transação e, por isso, não pode ser excluída.");
        }

        categoriaRepository.deleteById(id);
    }

    /**
     * Obtém o usuário atualmente autenticado no sistema.
     *
     * @return usuário autenticado
     */
    private Usuario obterUsuarioLogado() {
        return UsuarioAutenticadoService.obterUsuario();
    }

    /**
     * Verifica se a categoria pertence ao usuário atualmente autenticado.
     * Lança exceção caso contrário, impedindo operação indevida.
     *
     * @param categoria categoria a ser verificada
     * @throws OperacaoInvalidaException se a categoria não pertencer ao usuário autenticado
     */
    private void verificaSeCategoriaFoiCriadaPeloUsuarioLogado(Categoria categoria) {
        String method = request.getMethod();

        Usuario usuarioLogado = obterUsuarioLogado();

        if(!categoria.getUsuario().equals(usuarioLogado)) {
            throw new OperacaoInvalidaException("Operação " + method + " inviável. A categoria " + categoria.getId() + " não pertence ao usuário " + usuarioLogado.getEmail());
        }
    }

    /**
     * Normaliza o nome da categoria aplicando regras de padronização.
     *
     * @param nome nome original da categoria
     * @return nome normalizado
     */
    private String normalizarNome(String nome) {
        return stringPadronization.converter(nome);
    }

}
