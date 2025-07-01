package io.github.devopMarkz.backend.financas.domain.model;

import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tb_transacao")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor", precision = 19, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "data_transacao", nullable = false)
    private LocalDate dataTransacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo tipo;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "paga", nullable = false)
    private Boolean paga = true;

    public Transacao() {
    }

    public Transacao(Categoria categoria, Usuario usuario, String descricao, BigDecimal valor, LocalDate dataTransacao, Tipo tipo, String observacoes) {
        this.categoria = categoria;
        this.usuario = usuario;
        this.descricao = descricao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.tipo = tipo;
        this.observacoes = observacoes;
    }

    public Transacao(Categoria categoria, Usuario usuario, String descricao, BigDecimal valor, LocalDate dataTransacao, Tipo tipo, String observacoes, Boolean paga) {
        this.categoria = categoria;
        this.usuario = usuario;
        this.descricao = descricao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.tipo = tipo;
        this.observacoes = observacoes;
        this.paga = paga;
    }

    public Transacao(Long id, Categoria categoria, Usuario usuario, String descricao, BigDecimal valor, LocalDate dataTransacao, Tipo tipo, String observacoes) {
        this.id = id;
        this.categoria = categoria;
        this.usuario = usuario;
        this.descricao = descricao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.tipo = tipo;
        this.observacoes = observacoes;
    }

    public Transacao(Long id, Categoria categoria, Usuario usuario, String descricao, BigDecimal valor, LocalDate dataTransacao, Tipo tipo, String observacoes, Boolean paga) {
        this.id = id;
        this.categoria = categoria;
        this.usuario = usuario;
        this.descricao = descricao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.tipo = tipo;
        this.observacoes = observacoes;
        this.paga = paga;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDate dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transacao transacao = (Transacao) o;
        return Objects.equals(id, transacao.id) && Objects.equals(categoria, transacao.categoria) && Objects.equals(usuario, transacao.usuario) && tipo == transacao.tipo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoria, usuario, tipo);
    }
}
