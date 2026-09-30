package br.com.onwebtec.habitare.imovel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ImovelCreateDTO(
        @NotBlank(message = "O código é obrigatório!")
        @Size(max = 30, message = "O código deve ter no máximo 30 caracteres.")
        String codigo,
        @NotBlank(message = "O título é obrigatório!")
        @Size(max = 120, message = "O título deve ter no máximo 120 caracteres.")
        String titulo,
        @NotBlank(message = "A cidade é obrigatória!")
        String cidade,
        @NotNull(message = "O preço é obrigatório!")
        @Positive(message = "O preço deve ser positivo.")
        BigDecimal preco
) {
}
