package br.com.onwebtec.habitare.corretor;

import br.com.onwebtec.habitare.corretor.dto.CorretorCreateDTO;
import br.com.onwebtec.habitare.corretor.dto.CorretorResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
interface CorretorMapper {
    default Corretor toEntity(CorretorCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Corretor.Builder(dto.nome(), dto.creci(), dto.telefone(), dto.email()).build();
    }

    CorretorResponseDTO toResponse(Corretor entity);

    List<CorretorResponseDTO> toResponseList(List<Corretor> entities);
}
