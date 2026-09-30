package br.com.onwebtec.habitare.corretor;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_corretores",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "creci"),
                @UniqueConstraint(columnNames = "email")
        })
class Corretor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(nullable = false, length = 20)
    private String creci;
    @Column(nullable = false)
    private String telefone;
    @Column(nullable = false)
    private String email;

    protected Corretor() {
    }

    private Corretor(Builder builder) {
        this.id = builder.id;
        this.nome = builder.nome;
        this.creci = builder.creci;
        this.telefone = builder.telefone;
        this.email = builder.email;
    }

    public void atualizarDados(String nome, String creci, String telefone, String email) {
        Objects.requireNonNull(nome, "O nome é obrigatório!");
        Objects.requireNonNull(creci, "O CRECI é obrigatório!");
        Objects.requireNonNull(telefone, "O telefone é obrigatório!");
        Objects.requireNonNull(email, "O e-mail é obrigatório!");
        this.nome = nome;
        this.creci = creci;
        this.telefone = telefone;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCreci() { return creci; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corretor corretor = (Corretor) o;
        return id != null && id.equals(corretor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creci);
    }

    public static class Builder {
        private final String nome;
        private final String creci;
        private final String telefone;
        private final String email;
        private Long id;

        public Builder(String nome, String creci, String telefone, String email) {
            if (nome == null || nome.isBlank()) {
                throw new IllegalArgumentException("O nome é obrigatório!");
            }
            if (creci == null || creci.isBlank()) {
                throw new IllegalArgumentException("O CRECI é obrigatório!");
            }
            if (telefone == null || telefone.isBlank()) {
                throw new IllegalArgumentException("O telefone é obrigatório!");
            }
            if (email == null || email.isBlank()) {
                throw new IllegalArgumentException("O e-mail é obrigatório!");
            }
            this.nome = nome;
            this.creci = creci;
            this.telefone = telefone;
            this.email = email;
        }

        public Builder comId(Long id) {
            this.id = id;
            return this;
        }

        public Corretor build() {
            return new Corretor(this);
        }
    }
}
