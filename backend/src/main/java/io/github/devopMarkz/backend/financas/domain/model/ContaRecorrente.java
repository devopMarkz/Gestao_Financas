package io.github.devopMarkz.backend.financas.domain.model;

import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "tb_conta_recorrente")
public class ContaRecorrente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "dia_vencimento", nullable = false)
    private Short diaVencimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo tipo;

    @Column(name = "ativa", nullable = false)
    private Boolean ativa = true;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    public ContaRecorrente() {
    }

    public ContaRecorrente(Long id, Usuario usuario, Categoria categoria, String descricao, BigDecimal valor, Short diaVencimento, Tipo tipo, Boolean ativa, String observacoes) {
        this.id = id;
        this.usuario = usuario;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.diaVencimento = diaVencimento;
        this.tipo = tipo;
        this.ativa = ativa;
        this.observacoes = observacoes;
    }

    public ContaRecorrente(Usuario usuario, Categoria categoria, String descricao, BigDecimal valor, Short diaVencimento, Tipo tipo, Boolean ativa, String observacoes) {
        this.usuario = usuario;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.diaVencimento = diaVencimento;
        this.tipo = tipo;
        this.ativa = ativa;
        this.observacoes = observacoes;
    }

    public ContaRecorrente(Usuario usuario, Categoria categoria, String descricao, BigDecimal valor, Short diaVencimento, Tipo tipo, Boolean ativa) {
        this.usuario = usuario;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.diaVencimento = diaVencimento;
        this.tipo = tipo;
        this.ativa = ativa;
    }

    public ContaRecorrente(Usuario usuario, Categoria categoria, String descricao, BigDecimal valor, Short diaVencimento, Tipo tipo) {
        this.usuario = usuario;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.diaVencimento = diaVencimento;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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

    public Short getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(Short diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
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
        ContaRecorrente that = (ContaRecorrente) o;
        return Objects.equals(id, that.id) && Objects.equals(usuario, that.usuario) && Objects.equals(categoria, that.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuario, categoria);
    }
}
