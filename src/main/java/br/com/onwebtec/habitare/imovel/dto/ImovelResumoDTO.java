package br.com.onwebtec.habitare.imovel.dto;

public record ImovelResumoDTO(
        Long id,
        String codigo,
        ImovelStatusDTO status
) {
}
