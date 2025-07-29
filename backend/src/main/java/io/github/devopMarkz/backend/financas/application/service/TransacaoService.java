package io.github.devopMarkz.backend.financas.application.service;

import io.github.devopMarkz.backend.financas.application.dto.transacao.TransacaoRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.transacao.TransacaoResponseDTO;
import io.github.devopMarkz.backend.financas.application.mappers.TransacaoMapper;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import io.github.devopMarkz.backend.financas.domain.model.Transacao;
import io.github.devopMarkz.backend.financas.domain.repository.TransacaoRepository;
import io.github.devopMarkz.backend.financas.infraestrutucture.exception.EntidadeInexistenteException;
import io.github.devopMarkz.backend.financas.infraestrutucture.exception.OperacaoInvalidaException;
import io.github.devopMarkz.backend.shared.config.UsuarioAutenticadoService;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Serviço responsável pelas operações de negócio relacionadas às Transações financeiras.

 * Inclui criação, busca, atualização e exclusão de transações,
 * assegurando que as operações sejam realizadas apenas pelo usuário proprietário,
 * e validando a coerência entre tipo de transação e categoria.
 *
 * @author devopMarkz
 */
@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final HttpServletRequest request;
    private final TransacaoMapper transacaoMapper;

    public TransacaoService(TransacaoRepository transacaoRepository,
                            HttpServletRequest request,
                            TransacaoMapper transacaoMapper) {
        this.transacaoRepository = transacaoRepository;
        this.request = request;
        this.transacaoMapper = transacaoMapper;
    }

    /**
     * Salva uma nova transação associada ao usuário autenticado,
     * validando se a categoria existe e se o tipo da transação é compatível com o da categoria.
     *
     * @param transacaoRequest DTO com dados da transação a ser criada
     * @return DTO com dados da transação salva
     * @throws EntidadeInexistenteException se a categoria não existir
     * @throws OperacaoInvalidaException se o tipo da transação for diferente do tipo da categoria
     */
    @Transactional
    public TransacaoResponseDTO salvar(TransacaoRequestDTO transacaoRequest) {
        Transacao transacao = transacaoMapper.toTransacao(transacaoRequest);

        if(!transacao.getTipo().name().equalsIgnoreCase(transacao.getCategoria().getTipo().name())) {
            throw new OperacaoInvalidaException("O tipo '" + transacao.getTipo().name() + "' da transação deve ser igual ao tipo '" + transacao.getCategoria().getTipo().name() + "' da categoria.");
        }

        transacao.setUsuario(UsuarioAutenticadoService.obterUsuario());

        transacao = transacaoRepository.save(transacao);

        return transacaoMapper.toTransacaoResponseDTO(transacao);
    }

    /**
     * Busca transações filtradas pelos parâmetros informados,
     * retornando uma página de resultados para o usuário autenticado.
     *
     * @param descricao filtro opcional pela descrição da transação
     * @param categoriaId filtro opcional pela categoria
     * @param tipo filtro opcional pelo tipo da transação
     * @param valorMin filtro opcional pelo valor mínimo
     * @param paga filtro opcional para transações pagas ou não
     * @param dataMin filtro opcional pela data mínima da transação
     * @param dataMax filtro opcional pela data máxima da transação
     * @param pageNumber número da página para paginação
     * @param pageSize tamanho da página para paginação
     * @return página de DTOs das transações filtradas
     */
    @Transactional(readOnly = true)
    public Page<TransacaoResponseDTO> findCatgoriasByFilter(String descricao, Long categoriaId, Tipo tipo, BigDecimal valorMin, Boolean paga, LocalDate dataMin, LocalDate dataMax, int pageNumber, int pageSize) {
        Page<Transacao> transacoes = transacaoRepository.filtrarTransacoes(
                UsuarioAutenticadoService.obterUsuario().getId(), descricao, categoriaId, tipo, valorMin, paga, dataMin, dataMax,
                PageRequest.of(pageNumber, pageSize));
        return transacoes.map(transacaoMapper::toTransacaoResponseDTO);
    }

    /**
     * Busca uma transação pelo seu ID, garantindo que pertença ao usuário autenticado.
     *
     * @param id identificador da transação
     * @return DTO da transação encontrada
     * @throws EntidadeInexistenteException se a transação não existir
     * @throws OperacaoInvalidaException se a transação não pertencer ao usuário
     */
    @Transactional(readOnly = true)
    public TransacaoResponseDTO findById(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeInexistenteException("Transação inexistente!"));

        verificaSeTransacaoFoiCriadaPeloUsuarioLogado(transacao);

        return transacaoMapper.toTransacaoResponseDTO(transacao);
    }

    /**
     * Atualiza uma transação existente pelo ID,
     * validando propriedade e coerência entre tipo da transação e categoria.
     *
     * @param id identificador da transação a ser atualizada
     * @param transacaoRequest DTO com dados para atualização
     * @return DTO da transação atualizada
     * @throws EntidadeInexistenteException se a transação ou categoria não existirem
     * @throws OperacaoInvalidaException se o tipo da transação for diferente do tipo da categoria
     */
    @Transactional
    public TransacaoResponseDTO atualizar(Long id, TransacaoRequestDTO transacaoRequest) {
        Transacao transacaoExistente = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeInexistenteException("Transação inexistente!"));

        verificaSeTransacaoFoiCriadaPeloUsuarioLogado(transacaoExistente);

        transacaoExistente = transacaoMapper.toTransacaoUpdated(transacaoRequest);

        if (!transacaoExistente.getTipo().name().equalsIgnoreCase(transacaoExistente.getCategoria().getTipo().name())) {
            throw new OperacaoInvalidaException(
                    "O tipo '" + transacaoExistente.getTipo().name() +
                            "' da transação deve ser igual ao tipo '" + transacaoExistente.getCategoria().getTipo().name() + "' da categoria."
            );
        }

        Transacao atualizado = transacaoRepository.save(transacaoExistente);

        return transacaoMapper.toTransacaoResponseDTO(atualizado);
    }

    /**
     * Remove uma transação pelo ID, assegurando que pertença ao usuário autenticado.
     *
     * @param id identificador da transação a ser deletada
     * @throws EntidadeInexistenteException se a transação não existir
     * @throws OperacaoInvalidaException se a transação não pertencer ao usuário
     */
    @Transactional
    public void deletar(Long id) {
        Transacao transacaoExistente = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeInexistenteException("Transação inexistente!"));

        verificaSeTransacaoFoiCriadaPeloUsuarioLogado(transacaoExistente);

        transacaoRepository.delete(transacaoExistente);
    }

    /**
     * Verifica se a transação pertence ao usuário atualmente autenticado,
     * lançando exceção caso contrário para evitar operações indevidas.
     *
     * @param transacao transação a ser verificada
     * @throws OperacaoInvalidaException se a transação não pertencer ao usuário logado
     */
    private void verificaSeTransacaoFoiCriadaPeloUsuarioLogado(Transacao transacao) {
        String method = request.getMethod();

        Usuario usuarioLogado = UsuarioAutenticadoService.obterUsuario();

        if(!transacao.getUsuario().equals(usuarioLogado)) {
            throw new OperacaoInvalidaException("Operação " + method + " inviável. A transação " + transacao.getId() + " não pertence ao usuário " + usuarioLogado.getEmail());
        }
    }

}
