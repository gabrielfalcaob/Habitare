package br.com.onwebtec.habitare.imovel;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "tb_imoveis", uniqueConstraints = @UniqueConstraint(columnNames = "codigo"))
class Imovel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String codigo;
    @Column(nullable = false, length = 120)
    private String titulo;
    @Column(nullable = false)
    private String cidade;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ImovelStatus status;

    protected Imovel() {
    }

    private Imovel(Builder builder) {
        this.id = builder.id;
        this.codigo = builder.codigo;
        this.titulo = builder.titulo;
        this.cidade = builder.cidade;
        this.preco = builder.preco;
        this.status = builder.status;
    }

    public void atualizarDados(String codigo, String titulo, String cidade, BigDecimal preco) {
        Objects.requireNonNull(codigo, "O código é obrigatório!");
        Objects.requireNonNull(titulo, "O título é obrigatório!");
        Objects.requireNonNull(cidade, "A cidade é obrigatória!");
        Objects.requireNonNull(preco, "O preço é obrigatório!");
        if (preco.signum() <= 0) {
            throw new IllegalArgumentException("O preço deve ser positivo.");
        }
        this.codigo = codigo;
        this.titulo = titulo;
        this.cidade = cidade;
        this.preco = preco;
    }

    public void alterarStatus(ImovelStatus novoStatus) {
        Objects.requireNonNull(novoStatus, "O status é obrigatório!");
        this.status = novoStatus;
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getCidade() { return cidade; }
    public BigDecimal getPreco() { return preco; }
    public ImovelStatus getStatus() { return status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Imovel imovel = (Imovel) o;
        return id != null && id.equals(imovel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    public static class Builder {
        private final String codigo;
        private final String titulo;
        private final String cidade;
        private final BigDecimal preco;
        private Long id;
        private ImovelStatus status = ImovelStatus.DISPONIVEL;

        public Builder(String codigo, String titulo, String cidade, BigDecimal preco) {
            if (codigo == null || codigo.isBlank()) {
                throw new IllegalArgumentException("O código é obrigatório!");
            }
            if (titulo == null || titulo.isBlank()) {
                throw new IllegalArgumentException("O título é obrigatório!");
            }
            if (cidade == null || cidade.isBlank()) {
                throw new IllegalArgumentException("A cidade é obrigatória!");
            }
            if (preco == null || preco.signum() <= 0) {
                throw new IllegalArgumentException("O preço é obrigatório e deve ser positivo!");
            }
            this.codigo = codigo;
            this.titulo = titulo;
            this.cidade = cidade;
            this.preco = preco;
        }

        public Builder comId(Long id) {
            this.id = id;
            return this;
        }

        public Builder comStatus(ImovelStatus status) {
            if (status != null) {
                this.status = status;
            }
            return this;
        }

        public Imovel build() {
            return new Imovel(this);
        }
    }
}
