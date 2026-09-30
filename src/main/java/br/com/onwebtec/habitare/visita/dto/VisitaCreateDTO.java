package br.com.onwebtec.habitare.visita.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record VisitaCreateDTO(
        @NotNull(message = "O imóvel é obrigatório!")
        Long imovelId,
        @NotNull(message = "A data e hora são obrigatórias!")
        @Future(message = "A data e hora da visita devem estar no futuro.")
        LocalDateTime dataHora,
        @Size(max = 255, message = "A observação deve ter no máximo 255 caracteres.")
        String observacao
) {
}
