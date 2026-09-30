package br.com.onwebtec.habitare.visita;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_visitas")
class Visita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_imovel", nullable = false)
    private Long imovelId;
    @Column(name = "id_usuario", nullable = false)
    private Long usuarioId;
    @Column(nullable = false)
    private LocalDateTime dataHora;
    @Column(length = 255)
    private String observacao;

    protected Visita() {
    }

    private Visita(Builder builder) {
        this.id = builder.id;
        this.imovelId = builder.imovelId;
        this.usuarioId = builder.usuarioId;
        this.dataHora = builder.dataHora;
        this.observacao = builder.observacao;
    }

    public Long getId() { return id; }
    public Long getImovelId() { return imovelId; }
    public Long getUsuarioId() { return usuarioId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getObservacao() { return observacao; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visita visita = (Visita) o;
        return id != null && id.equals(visita.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imovelId, usuarioId, dataHora);
    }

    public static class Builder {
        private final Long imovelId;
        private final Long usuarioId;
        private final LocalDateTime dataHora;
        private Long id;
        private String observacao;

        public Builder(Long imovelId, Long usuarioId, LocalDateTime dataHora) {
            this.imovelId = Objects.requireNonNull(imovelId, "O imóvel é obrigatório!");
            this.usuarioId = Objects.requireNonNull(usuarioId, "O usuário é obrigatório!");
            this.dataHora = Objects.requireNonNull(dataHora, "A data e hora são obrigatórias!");
        }

        public Builder comId(Long id) {
            this.id = id;
            return this;
        }

        public Builder comObservacao(String observacao) {
            this.observacao = observacao;
            return this;
        }

        public Visita build() {
            return new Visita(this);
        }
    }
}
