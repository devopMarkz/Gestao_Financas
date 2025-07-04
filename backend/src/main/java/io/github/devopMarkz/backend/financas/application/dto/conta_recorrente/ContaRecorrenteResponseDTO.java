package io.github.devopMarkz.backend.financas.application.dto.conta_recorrente;

import java.math.BigDecimal;

public class ContaRecorrenteResponseDTO {

    private Long id;
    private Long categoriaId;
    private String categoriaNome;
    private String descricao;
    private BigDecimal valor;
    private Short diaVencimento;
    private String tipo;
    private Boolean ativa;
    private String observacoes;

    public ContaRecorrenteResponseDTO() {
    }

    public ContaRecorrenteResponseDTO(Long id, Long categoriaId, String categoriaNome, String descricao, BigDecimal valor, Short diaVencimento, String tipo, Boolean ativa, String observacoes) {
        this.id = id;
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
        this.descricao = descricao;
        this.valor = valor;
        this.diaVencimento = diaVencimento;
        this.tipo = tipo;
        this.ativa = ativa;
        this.observacoes = observacoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
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
}