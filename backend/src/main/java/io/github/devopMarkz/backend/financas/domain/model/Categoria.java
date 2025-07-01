package io.github.devopMarkz.backend.financas.domain.model;

import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo tipo;

    @Column(name = "ativa", nullable = false)
    private Boolean ativa = true;

    public Categoria() {
    }

    public Categoria(Usuario usuario, String nome, Tipo tipo) {
        this.usuario = usuario;
        this.nome = nome;
        this.tipo = tipo;
    }

    public Categoria(Long id, Usuario usuario, String nome, Tipo tipo, Boolean ativa) {
        this.id = id;
        this.usuario = usuario;
        this.nome = nome;
        this.tipo = tipo;
        this.ativa = ativa;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(id, categoria.id) && Objects.equals(usuario, categoria.usuario) && Objects.equals(nome, categoria.nome) && tipo == categoria.tipo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuario, nome, tipo);
    }
}
