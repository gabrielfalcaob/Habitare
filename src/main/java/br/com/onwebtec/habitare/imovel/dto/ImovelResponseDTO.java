package br.com.onwebtec.habitare.imovel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ImovelResponseDTO(
        @NotNull(message = "O identificador não pode ser nulo na resposta.")
        Long id,
        @NotBlank(message = "O código não pode ser vazio na resposta.")
        String codigo,
        @NotBlank(message = "O título não pode ser vazio na resposta.")
        String titulo,
        @NotBlank(message = "A cidade não pode ser vazia na resposta.")
        String cidade,
        @NotNull(message = "O preço não pode ser nulo na resposta.")
        @Positive(message = "O preço deve ser positivo na resposta.")
        BigDecimal preco,
        @NotNull(message = "O status não pode ser nulo na resposta.")
        ImovelStatusDTO status
) {
}
