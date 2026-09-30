package br.com.onwebtec.habitare.corretor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CorretorResponseDTO(
        @NotNull(message = "O identificador não pode ser nulo na resposta.")
        Long id,
        @NotBlank(message = "O nome não pode ser vazio na resposta.")
        String nome,
        @NotBlank(message = "O CRECI não pode ser vazio na resposta.")
        String creci,
        @NotBlank(message = "O telefone não pode ser vazio na resposta.")
        String telefone,
        @NotBlank(message = "O e-mail não pode ser vazio na resposta.")
        @Email(message = "Formato de e-mail inválido")
        String email
) {
}
