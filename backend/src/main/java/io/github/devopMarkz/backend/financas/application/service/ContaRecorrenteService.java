package io.github.devopMarkz.backend.financas.application.service;

import io.github.devopMarkz.backend.financas.application.dto.conta_recorrente.ContaRecorrenteRequestDTO;
import io.github.devopMarkz.backend.financas.application.dto.conta_recorrente.ContaRecorrenteResponseDTO;
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

@Service
public class ContaRecorrenteService {

    private final ContaRecorrenteRepository contaRecorrenteRepository;
    private final CategoriaRepository categoriaRepository;
    private final TransacaoRepository transacaoRepository;

    public ContaRecorrenteService(ContaRecorrenteRepository contaRecorrenteRepository,
                                  CategoriaRepository categoriaRepository,
                                  TransacaoRepository transacaoRepository) {
        this.contaRecorrenteRepository = contaRecorrenteRepository;
        this.categoriaRepository = categoriaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public ContaRecorrenteResponseDTO salvar(ContaRecorrenteRequestDTO dto) {
        Usuario usuario = UsuarioAutenticadoService.obterUsuario();

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new EntidadeInexistenteException("Categoria inexistente."));

        ContaRecorrente conta = new ContaRecorrente(usuario, categoria, dto.getDescricao(), dto.getValor(), dto.getDiaVencimento(), dto.getTipo(), dto.getAtiva(), dto.getObservacoes());

        conta = contaRecorrenteRepository.save(conta);
        return toResponseDTO(conta);
    }

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

        return contas.map(this::converterParaResponseDTO);
    }

    @Transactional(readOnly = true)
    public ContaRecorrenteResponseDTO buscarPorId(Long id) {
        Usuario usuario = UsuarioAutenticadoService.obterUsuario();

        ContaRecorrente conta = contaRecorrenteRepository.buscarPorId(id, usuario.getId())
                .orElseThrow(() -> new EntidadeInexistenteException("Conta recorrente não encontrada."));

        return toResponseDTO(conta);
    }

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

        return toResponseDTO(contaRecorrenteRepository.save(conta));
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = UsuarioAutenticadoService.obterUsuario();

        ContaRecorrente conta = contaRecorrenteRepository.buscarPorId(usuario.getId(), id)
                .orElseThrow(() -> new EntidadeInexistenteException("Conta recorrente não encontrada."));

        contaRecorrenteRepository.delete(conta);
    }

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

    private ContaRecorrenteResponseDTO toResponseDTO(ContaRecorrente conta) {
        ContaRecorrenteResponseDTO dto = new ContaRecorrenteResponseDTO();
        dto.setId(conta.getId());
        dto.setCategoriaId(conta.getCategoria().getId());
        dto.setCategoriaNome(conta.getCategoria().getNome());
        dto.setDescricao(conta.getDescricao());
        dto.setValor(conta.getValor());
        dto.setDiaVencimento(conta.getDiaVencimento());
        dto.setTipo(conta.getTipo().name());
        dto.setAtiva(conta.getAtiva());
        dto.setObservacoes(conta.getObservacoes());
        return dto;
    }

    private ContaRecorrenteResponseDTO converterParaResponseDTO(ContaRecorrente conta) {
        ContaRecorrenteResponseDTO dto = new ContaRecorrenteResponseDTO();
        dto.setId(conta.getId());
        dto.setDescricao(conta.getDescricao());
        dto.setValor(conta.getValor());
        dto.setDiaVencimento(conta.getDiaVencimento());
        dto.setTipo(conta.getTipo().name());
        dto.setAtiva(conta.getAtiva());
        dto.setObservacoes(conta.getObservacoes());

        dto.setCategoriaId(conta.getCategoria().getId());
        dto.setCategoriaNome(conta.getCategoria().getNome());

        return dto;
    }
}