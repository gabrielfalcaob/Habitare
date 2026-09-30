package br.com.onwebtec.habitare.visita;

import br.com.onwebtec.habitare.visita.dto.VisitaCreateDTO;
import br.com.onwebtec.habitare.visita.dto.VisitaResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
interface VisitaMapper {
    default Visita toEntity(VisitaCreateDTO dto, Long usuarioId) {
        if (dto == null) {
            return null;
        }
        return new Visita.Builder(dto.imovelId(), usuarioId, dto.dataHora())
                .comObservacao(dto.observacao())
                .build();
    }

    VisitaResponseDTO toResponse(Visita entity);

    List<VisitaResponseDTO> toResponseList(List<Visita> entities);
}
