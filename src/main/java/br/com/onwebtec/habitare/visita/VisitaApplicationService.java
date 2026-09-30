package br.com.onwebtec.habitare.visita;

import br.com.onwebtec.habitare.shared.exception.EntidadeNaoEncontradaException;
import br.com.onwebtec.habitare.visita.dto.VisitaCreateDTO;
import br.com.onwebtec.habitare.visita.dto.VisitaResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class VisitaApplicationService {
    private final VisitaService visitaDomainService;
    private final VisitaRepository visitaRepository;
    private final VisitaMapper mapper;

    VisitaApplicationService(VisitaService visitaDomainService,
                             VisitaRepository visitaRepository,
                             VisitaMapper mapper) {
        this.visitaDomainService = visitaDomainService;
        this.visitaRepository = visitaRepository;
        this.mapper = mapper;
    }

    @Transactional
    public VisitaResponseDTO agendar(VisitaCreateDTO dto, Long usuarioId) {
        Visita novaVisita = mapper.toEntity(dto, usuarioId);
        visitaDomainService.validarAgendamento(novaVisita);
        return mapper.toResponse(visitaRepository.save(novaVisita));
    }

    @Transactional(readOnly = true)
    public List<VisitaResponseDTO> listarDoUsuario(Long usuarioId) {
        return mapper.toResponseList(visitaRepository.findByUsuarioId(usuarioId));
    }

    @Transactional
    public void cancelar(Long visitaId, Long usuarioId) {
        Visita visita = visitaRepository.findByIdAndUsuarioId(visitaId, usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Visita não encontrada para o identificador: " + visitaId));
        visitaRepository.delete(visita);
    }
}
