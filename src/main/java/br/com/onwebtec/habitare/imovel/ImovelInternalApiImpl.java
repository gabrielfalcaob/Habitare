package br.com.onwebtec.habitare.imovel;

import br.com.onwebtec.habitare.imovel.dto.ImovelResumoDTO;
import br.com.onwebtec.habitare.shared.exception.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
class ImovelInternalApiImpl implements ImovelInternalApi {
    private final ImovelRepository repository;

    ImovelInternalApiImpl(ImovelRepository repository) {
        this.repository = repository;
    }

    @Override
    public ImovelResumoDTO buscarResumo(Long id) {
        return repository.findById(id)
                .map(i -> new ImovelResumoDTO(i.getId(), i.getCodigo(),
                        br.com.onwebtec.habitare.imovel.dto.ImovelStatusDTO.fromDomain(i.getStatus())))
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Imóvel não encontrado para o identificador: " + id));
    }
}
