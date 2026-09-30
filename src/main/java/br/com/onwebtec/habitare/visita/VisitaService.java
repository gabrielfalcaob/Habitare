package br.com.onwebtec.habitare.visita;

import br.com.onwebtec.habitare.imovel.ImovelInternalApi;
import br.com.onwebtec.habitare.imovel.dto.ImovelResumoDTO;
import br.com.onwebtec.habitare.imovel.dto.ImovelStatusDTO;
import br.com.onwebtec.habitare.shared.exception.ConflitoException;
import br.com.onwebtec.habitare.shared.exception.OperacaoInvalidaException;
import org.springframework.stereotype.Service;

@Service
class VisitaService {
    private final VisitaRepository repository;
    private final ImovelInternalApi imovelInternalApi;

    VisitaService(VisitaRepository repository, ImovelInternalApi imovelInternalApi) {
        this.repository = repository;
        this.imovelInternalApi = imovelInternalApi;
    }

    public void validarAgendamento(Visita novaVisita) {
        ImovelResumoDTO imovel = imovelInternalApi.buscarResumo(novaVisita.getImovelId());
        if (imovel.status() != ImovelStatusDTO.DISPONIVEL) {
            throw new OperacaoInvalidaException(
                    "Só é possível agendar visita para imóvel DISPONIVEL. Situação atual: "
                            + imovel.status());
        }
        if (repository.existsByImovelIdAndDataHora(
                novaVisita.getImovelId(), novaVisita.getDataHora())) {
            throw new ConflitoException(
                    "Já existe uma visita agendada para este imóvel na data/hora informada.");
        }
    }
}
