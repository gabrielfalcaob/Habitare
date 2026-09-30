package br.com.onwebtec.habitare.corretor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CorretorCreateDTO(
        @NotBlank(message = "O nome é obrigatório!")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
        String nome,
        @NotBlank(message = "O CRECI é obrigatório!")
        @Size(max = 20, message = "O CRECI deve ter no máximo 20 caracteres.")
        String creci,
        @NotBlank(message = "O telefone é obrigatório!")
        String telefone,
        @NotBlank(message = "O e-mail é obrigatório!")
        @Email(message = "Formato de e-mail inválido")
        String email
) {
}
