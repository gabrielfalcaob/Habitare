package br.com.onwebtec.habitare.imovel;

import br.com.onwebtec.habitare.imovel.dto.ImovelCreateDTO;
import br.com.onwebtec.habitare.imovel.dto.ImovelResponseDTO;
import br.com.onwebtec.habitare.imovel.dto.ImovelResumoDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
interface ImovelMapper {
    default Imovel toEntity(ImovelCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Imovel.Builder(dto.codigo(), dto.titulo(), dto.cidade(), dto.preco()).build();
    }

    ImovelResponseDTO toResponse(Imovel entity);

    List<ImovelResponseDTO> toResponseList(List<Imovel> entities);

    ImovelResumoDTO toResumo(Imovel entity);
}
