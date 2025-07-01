package io.github.devopMarkz.backend.financas.application.dto.transacao;

import io.github.devopMarkz.backend.financas.application.dto.categoria.CategoriaResponseDTO;
import io.github.devopMarkz.backend.financas.domain.model.Tipo;

import java.time.LocalDate;

public class TransacaoResponseDTO {

    private Long id;
    private String descricao;
    private Double valor;
    private LocalDate dataTransacao;
    private Tipo tipo;
    private String observacoes;

    private CategoriaResponseDTO categoria;

    public TransacaoResponseDTO() {}

    public TransacaoResponseDTO(Long id, String descricao, Double valor, LocalDate dataTransacao, Tipo tipo, String observacoes, CategoriaResponseDTO categoria) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.tipo = tipo;
        this.observacoes = observacoes;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
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

    public CategoriaResponseDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaResponseDTO categoria) {
        this.categoria = categoria;
    }
}