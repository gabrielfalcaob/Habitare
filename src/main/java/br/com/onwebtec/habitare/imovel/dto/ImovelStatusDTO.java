package br.com.onwebtec.habitare.imovel.dto;

import br.com.onwebtec.habitare.imovel.ImovelStatus;

public enum ImovelStatusDTO {
    DISPONIVEL,
    RESERVADO,
    VENDIDO;

    public ImovelStatus toDomain() {
        return switch (this) {
            case DISPONIVEL -> ImovelStatus.DISPONIVEL;
            case RESERVADO -> ImovelStatus.RESERVADO;
            case VENDIDO -> ImovelStatus.VENDIDO;
        };
    }

    public static ImovelStatusDTO fromDomain(ImovelStatus status) {
        if (status == null) return null;
        return switch (status) {
            case DISPONIVEL -> ImovelStatusDTO.DISPONIVEL;
            case RESERVADO -> ImovelStatusDTO.RESERVADO;
            case VENDIDO -> ImovelStatusDTO.VENDIDO;
        };
    }
}
