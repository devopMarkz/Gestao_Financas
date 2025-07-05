package io.github.devopMarkz.backend.financas.application.dto.conta_recorrente;

import io.github.devopMarkz.backend.financas.domain.model.Tipo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ContaRecorrenteRequestDTO {

    @NotNull(message = "A categoria é obrigatória.")
    private Long categoriaId;

    @NotBlank(message = "A descrição é obrigatória.")
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    private String descricao;

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.01", inclusive = true, message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    @NotNull(message = "O dia de vencimento é obrigatório.")
    @Min(value = 1, message = "O dia de vencimento deve ser entre 1 e 31.")
    @Max(value = 31, message = "O dia de vencimento deve ser entre 1 e 31.")
    private Short diaVencimento;

    @NotNull(message = "O tipo é obrigatório.")
    private Tipo tipo;

    private Boolean ativa = true;

    @Size(max = 1000, message = "As observações devem ter no máximo 1000 caracteres.")
    private String observacoes;

    public ContaRecorrenteRequestDTO() {
    }

    public ContaRecorrenteRequestDTO(Long categoriaId, String descricao, BigDecimal valor, Short diaVencimento, Tipo tipo, String observacoes) {
        this.categoriaId = categoriaId;
        this.descricao = descricao;
        this.valor = valor;
        this.diaVencimento = diaVencimento;
        this.tipo = tipo;
        this.observacoes = observacoes;
    }

    public ContaRecorrenteRequestDTO(Long categoriaId, String descricao, BigDecimal valor, Short diaVencimento, Tipo tipo, Boolean ativa, String observacoes) {
        this.categoriaId = categoriaId;
        this.descricao = descricao;
        this.valor = valor;
        this.diaVencimento = diaVencimento;
        this.tipo = tipo;
        this.ativa = ativa;
        this.observacoes = observacoes;
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
}