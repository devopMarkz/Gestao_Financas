package io.github.devopMarkz.backend.financas.application.service;

import io.github.devopMarkz.backend.financas.application.dto.transacao.TransacaoRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.transacao.TransacaoResponseDTO;
import io.github.devopMarkz.backend.financas.domain.model.Categoria;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import io.github.devopMarkz.backend.financas.domain.model.Transacao;
import io.github.devopMarkz.backend.financas.domain.repository.CategoriaRepository;
import io.github.devopMarkz.backend.financas.domain.repository.TransacaoRepository;
import io.github.devopMarkz.backend.financas.infraestrutucture.exception.EntidadeInexistenteException;
import io.github.devopMarkz.backend.financas.infraestrutucture.exception.OperacaoInvalidaException;
import io.github.devopMarkz.backend.shared.config.UsuarioAutenticadoService;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import io.github.devopMarkz.backend.usuario.domain.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private HttpServletRequest request;

    public TransacaoService(TransacaoRepository transacaoRepository,
                            CategoriaRepository categoriaRepository,
                            UsuarioRepository usuarioRepository,
                            ModelMapper modelMapper,
                            HttpServletRequest request) {
        this.transacaoRepository = transacaoRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.request = request;
    }

    @Transactional
    public TransacaoResponseDTO salvar(TransacaoRequestDTO transacaoRequest) {
        Transacao transacao = new Transacao();
        transacao.setDescricao(transacaoRequest.getDescricao());
        transacao.setValor(transacaoRequest.getValor());
        transacao.setDataTransacao(transacaoRequest.getDataTransacao());
        transacao.setTipo(transacaoRequest.getTipo());
        transacao.setObservacoes(transacaoRequest.getObservacoes());
        transacao.setPaga(transacaoRequest.getPaga());

        Categoria categoria = categoriaRepository.findById(transacaoRequest.getCategoriaId())
                .orElseThrow(() -> new EntidadeInexistenteException("Categoria Inexistente!"));

        if(!transacao.getTipo().name().equalsIgnoreCase(categoria.getTipo().name())) {
            throw new OperacaoInvalidaException("O tipo '" + transacao.getTipo().name() + "' da transação deve ser igual ao tipo '" + categoria.getTipo().name() + "' da categoria.");
        }

        transacao.setCategoria(categoria);
        transacao.setUsuario(UsuarioAutenticadoService.obterUsuario());

        transacao = transacaoRepository.save(transacao);

        return modelMapper.map(transacao, TransacaoResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public Page<TransacaoResponseDTO> findCatgoriasByFilter(String descricao, Long categoriaId, Tipo tipo, BigDecimal valorMin, Boolean paga, LocalDate dataMin, LocalDate dataMax, int pageNumber, int pageSize) {
        Page<Transacao> transacoes = transacaoRepository.filtrarTransacoes(UsuarioAutenticadoService.obterUsuario().getId(), descricao, categoriaId, tipo, valorMin, paga, dataMin, dataMax, PageRequest.of(pageNumber, pageSize));
        return transacoes.map(transacao -> modelMapper.map(transacao, TransacaoResponseDTO.class));
    }

    @Transactional(readOnly = true)
    public TransacaoResponseDTO findById(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeInexistenteException("Transação inexistente!"));

        verificaSeTransacaoFoiCriadaPeloUsuarioLogado(transacao);

        return modelMapper.map(transacao, TransacaoResponseDTO.class);
    }

    @Transactional
    public TransacaoResponseDTO atualizar(Long id, TransacaoRequestDTO transacaoRequest) {
        Transacao transacaoExistente = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeInexistenteException("Transação inexistente!"));

        verificaSeTransacaoFoiCriadaPeloUsuarioLogado(transacaoExistente);

        modelMapper.map(transacaoRequest, transacaoExistente);

        Categoria categoria = categoriaRepository.findById(transacaoRequest.getCategoriaId())
                .orElseThrow(() -> new EntidadeInexistenteException("Categoria inexistente!"));

        if (!transacaoExistente.getTipo().name().equalsIgnoreCase(categoria.getTipo().name())) {
            throw new OperacaoInvalidaException(
                    "O tipo '" + transacaoExistente.getTipo().name() +
                            "' da transação deve ser igual ao tipo '" + categoria.getTipo().name() + "' da categoria."
            );
        }
        transacaoExistente.setCategoria(categoria);

        Transacao atualizado = transacaoRepository.save(transacaoExistente);

        return modelMapper.map(atualizado, TransacaoResponseDTO.class);
    }

    @Transactional
    public void deletar(Long id) {
        Transacao transacaoExistente = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeInexistenteException("Transação inexistente!"));

        verificaSeTransacaoFoiCriadaPeloUsuarioLogado(transacaoExistente);

        transacaoRepository.delete(transacaoExistente);
    }

    private void verificaSeTransacaoFoiCriadaPeloUsuarioLogado(Transacao transacao) {
        String method = request.getMethod();

        Usuario usuarioLogado = UsuarioAutenticadoService.obterUsuario();

        if(!transacao.getUsuario().equals(usuarioLogado)) {
            throw new OperacaoInvalidaException("Operação " + method + " inviável. A transação " + transacao.getId() + " não pertence ao usuário " + usuarioLogado.getEmail());
        }
    }

}
