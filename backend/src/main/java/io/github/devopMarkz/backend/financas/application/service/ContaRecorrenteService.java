package io.github.devopMarkz.backend.financas.application.service;

import io.github.devopMarkz.backend.financas.application.dto.conta_recorrente.ContaRecorrenteRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.conta_recorrente.ContaRecorrenteResponseDTO;
import io.github.devopMarkz.backend.financas.application.mappers.ContaRecorrenteMapper;
import io.github.devopMarkz.backend.financas.domain.model.Categoria;
import io.github.devopMarkz.backend.financas.domain.model.ContaRecorrente;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import io.github.devopMarkz.backend.financas.domain.model.Transacao;
import io.github.devopMarkz.backend.financas.domain.repository.CategoriaRepository;
import io.github.devopMarkz.backend.financas.domain.repository.ContaRecorrenteRepository;
import io.github.devopMarkz.backend.financas.domain.repository.TransacaoRepository;
import io.github.devopMarkz.backend.financas.infraestrutucture.exception.EntidadeInexistenteException;
import io.github.devopMarkz.backend.shared.config.UsuarioAutenticadoService;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Serviço responsável pela lógica de negócio relacionada às Contas Recorrentes.
 * Gerencia operações de criação, busca, atualização, exclusão e criação de transações a partir de contas recorrentes,
 * garantindo que as operações sejam feitas somente pelo usuário autenticado e que categorias relacionadas existam.
 *
 * @author devopMarkz
 */
@Service
public class ContaRecorrenteService {

    private final ContaRecorrenteRepository contaRecorrenteRepository;
    private final CategoriaRepository categoriaRepository;
    private final TransacaoRepository transacaoRepository;
    private final ContaRecorrenteMapper contaRecorrenteMapper;

    public ContaRecorrenteService(ContaRecorrenteRepository contaRecorrenteRepository,
                                  CategoriaRepository categoriaRepository,
                                  TransacaoRepository transacaoRepository,
                                  ContaRecorrenteMapper contaRecorrenteMapper) {
        this.contaRecorrenteRepository = contaRecorrenteRepository;
        this.categoriaRepository = categoriaRepository;
        this.transacaoRepository = transacaoRepository;
        this.contaRecorrenteMapper = contaRecorrenteMapper;
    }

    /**
     * Salva uma nova conta recorrente associada ao usuário autenticado,
     * garantindo que a categoria exista.
     *
     * @param dto Dados da conta recorrente para criação
     * @return DTO com dados da conta criada
     * @throws EntidadeInexistenteException se a categoria informada não existir
     */
    @Transactional
    public ContaRecorrenteResponseDTO salvar(ContaRecorrenteRequestDTO dto) {
        Usuario usuario = UsuarioAutenticadoService.obterUsuario();

        ContaRecorrente conta = contaRecorrenteMapper.toContaRecorrente(dto);
        conta.setUsuario(usuario);

        conta = contaRecorrenteRepository.save(conta);
        return contaRecorrenteMapper.toContaRecorrenteResponseDTO(conta);
    }

    /**
     * Busca contas recorrentes filtradas pelos parâmetros fornecidos,
     * limitando o acesso às contas do usuário autenticado.
     *
     * @param descricao filtro opcional pela descrição da conta
     * @param categoriaId filtro opcional pela categoria
     * @param tipo filtro opcional pelo tipo da conta
     * @param ativa filtro opcional pelo status ativo/inativo
     * @param diaVencimento filtro opcional pelo dia de vencimento
     * @param pageable objeto de paginação e ordenação
     * @return página de contas recorrentes filtradas
     */
    @Transactional(readOnly = true)
    public Page<ContaRecorrenteResponseDTO> buscarContasFiltradas(
            String descricao,
            Long categoriaId,
            Tipo tipo,
            Boolean ativa,
            Short diaVencimento,
            Pageable pageable) {

        Usuario usuario = UsuarioAutenticadoService.obterUsuario();

        Page<ContaRecorrente> contas = contaRecorrenteRepository.buscarComFiltros(
                usuario.getId(), descricao, categoriaId, tipo, ativa, diaVencimento, pageable
        );

        return contas.map(contaRecorrenteMapper::toContaRecorrenteResponseDTO);
    }

    /**
     * Busca uma conta recorrente pelo seu ID,
     * garantindo que pertença ao usuário autenticado.
     *
     * @param id identificador da conta recorrente
     * @return DTO da conta encontrada
     * @throws EntidadeInexistenteException se a conta não existir ou não pertencer ao usuário
     */
    @Transactional(readOnly = true)
    public ContaRecorrenteResponseDTO buscarPorId(Long id) {
        Usuario usuario = UsuarioAutenticadoService.obterUsuario();

        ContaRecorrente conta = contaRecorrenteRepository.buscarPorId(id, usuario.getId())
                .orElseThrow(() -> new EntidadeInexistenteException("Conta recorrente não encontrada."));

        return contaRecorrenteMapper.toContaRecorrenteResponseDTO(conta);
    }

    /**
     * Atualiza os dados de uma conta recorrente existente,
     * assegurando que a conta e a categoria existam e pertençam ao usuário.
     *
     * @param id identificador da conta a ser atualizada
     * @param dto dados para atualização
     * @return DTO da conta atualizada
     * @throws EntidadeInexistenteException se a conta ou categoria não existirem
     */
    @Transactional
    public ContaRecorrenteResponseDTO atualizar(Long id, ContaRecorrenteRequestDTO dto) {
        Usuario usuario = UsuarioAutenticadoService.obterUsuario();

        ContaRecorrente conta = contaRecorrenteRepository.buscarPorId(usuario.getId(), id)
                .orElseThrow(() -> new EntidadeInexistenteException("Conta recorrente não encontrada."));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new EntidadeInexistenteException("Categoria inexistente."));

        conta.setCategoria(categoria);
        conta.setDescricao(dto.getDescricao());
        conta.setValor(dto.getValor());
        conta.setDiaVencimento(dto.getDiaVencimento());
        conta.setTipo(dto.getTipo());
        conta.setAtiva(dto.getAtiva());
        conta.setObservacoes(dto.getObservacoes());

        return contaRecorrenteMapper.toContaRecorrenteResponseDTO(contaRecorrenteRepository.save(conta));
    }

    /**
     * Remove uma conta recorrente, garantindo que pertença ao usuário autenticado.
     *
     * @param id identificador da conta a ser deletada
     * @throws EntidadeInexistenteException se a conta não existir ou não pertencer ao usuário
     */
    @Transactional
    public void deletar(Long id) {
        Usuario usuario = UsuarioAutenticadoService.obterUsuario();

        ContaRecorrente conta = contaRecorrenteRepository.buscarPorId(usuario.getId(), id)
                .orElseThrow(() -> new EntidadeInexistenteException("Conta recorrente não encontrada."));

        contaRecorrenteRepository.delete(conta);
    }

    /**
     * Cria uma transação a partir de uma conta recorrente,
     * utilizando o dia de vencimento da conta como data da transação.
     *
     * @param id identificador da conta recorrente
     * @return id da transação criada
     * @throws EntidadeInexistenteException se a conta recorrente não existir
     */
    @Transactional
    public Long criarTransacao(Long id){
        Usuario usuario = UsuarioAutenticadoService.obterUsuario();

        ContaRecorrente contaRecorrente = contaRecorrenteRepository.buscarPorId(usuario.getId(), id)
                .orElseThrow(() -> new EntidadeInexistenteException("Conta recorrente inexistente."));

        LocalDate hoje = LocalDate.now();

        LocalDate dataPagamento = LocalDate.of(hoje.getYear(), hoje.getMonth(), contaRecorrente.getDiaVencimento());

        Transacao transacao = toTransacao(contaRecorrente, dataPagamento);

        transacaoRepository.save(transacao);

        return transacao.getId();
    }

    private Transacao toTransacao(ContaRecorrente conta, LocalDate dataPagamento) {
        Transacao transacao = new Transacao();
        transacao.setCategoria(conta.getCategoria());
        transacao.setUsuario(conta.getUsuario());
        transacao.setDescricao(conta.getDescricao());
        transacao.setValor(conta.getValor());
        transacao.setTipo(conta.getTipo());
        transacao.setObservacoes(conta.getObservacoes());
        transacao.setDataTransacao(dataPagamento);

        return transacao;
    }
}
