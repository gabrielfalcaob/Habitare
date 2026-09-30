package br.com.onwebtec.habitare.imovel.dto;

import jakarta.validation.constraints.NotNull;

public record ImovelPatchDTO(
        @NotNull(message = "O status é obrigatório!")
        ImovelStatusDTO status
) {
}
