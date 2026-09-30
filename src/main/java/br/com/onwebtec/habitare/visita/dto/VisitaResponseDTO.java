package br.com.onwebtec.habitare.visita.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record VisitaResponseDTO(
        @NotNull(message = "O identificador não pode ser nulo na resposta.")
        Long id,
        @NotNull(message = "O imóvel não pode ser nulo na resposta.")
        Long imovelId,
        @NotNull(message = "O usuário não pode ser nulo na resposta.")
        Long usuarioId,
        @NotNull(message = "A data e hora não podem ser nulas na resposta.")
        LocalDateTime dataHora,
        String observacao
) {
}
