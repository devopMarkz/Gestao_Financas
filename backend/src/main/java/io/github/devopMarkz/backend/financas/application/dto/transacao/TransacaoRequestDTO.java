package io.github.devopMarkz.backend.financas.application.dto.transacao;

import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransacaoRequestDTO {

    @Positive(message = "Id informado deve ser um número positivo.")
    @NotNull(message = "Categoria não pode estar nula.")
    private Long categoriaId;

    @NotBlank(message = "Descrição não pode estar nula ou vazia.")
    private String descricao;

    @PositiveOrZero(message = "Valor deve ser positivo.")
    private BigDecimal valor;

    private LocalDate dataTransacao;

    private Tipo tipo;

    private String observacoes;

    private Boolean paga = true;

    public TransacaoRequestDTO() {
    }

    public TransacaoRequestDTO(Long categoriaId, String descricao, BigDecimal valor, LocalDate dataTransacao, Tipo tipo) {
        this.categoriaId = categoriaId;
        this.descricao = descricao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.tipo = tipo;
    }

    public TransacaoRequestDTO(Long categoriaId, String descricao, BigDecimal valor, LocalDate dataTransacao, Tipo tipo, String observacoes) {
        this.categoriaId = categoriaId;
        this.descricao = descricao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.tipo = tipo;
        this.observacoes = observacoes;
    }

    public TransacaoRequestDTO(Long categoriaId, String descricao, BigDecimal valor, LocalDate dataTransacao, Tipo tipo, Boolean paga) {
        this.categoriaId = categoriaId;
        this.descricao = descricao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.tipo = tipo;
        this.paga = paga;
    }

    public TransacaoRequestDTO(Long categoriaId, String descricao, BigDecimal valor, LocalDate dataTransacao, Tipo tipo, String observacoes, Boolean paga) {
        this.categoriaId = categoriaId;
        this.descricao = descricao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.tipo = tipo;
        this.observacoes = observacoes;
        this.paga = paga;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
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

    public Boolean getPaga() {
        return paga;
    }

    public void setPaga(Boolean paga) {
        this.paga = paga;
    }
}