package br.com.onwebtec.habitare.imovel;

import br.com.onwebtec.habitare.imovel.dto.ImovelCreateDTO;
import br.com.onwebtec.habitare.imovel.dto.ImovelPatchDTO;
import br.com.onwebtec.habitare.imovel.dto.ImovelResponseDTO;
import br.com.onwebtec.habitare.imovel.dto.ImovelUpdateDTO;
import br.com.onwebtec.habitare.shared.exception.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class ImovelApplicationService {
    private final ImovelService imovelDomainService;
    private final ImovelRepository imovelRepository;
    private final ImovelMapper mapper;

    ImovelApplicationService(ImovelService imovelDomainService,
                             ImovelRepository imovelRepository,
                             ImovelMapper mapper) {
        this.imovelDomainService = imovelDomainService;
        this.imovelRepository = imovelRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ImovelResponseDTO criarImovel(ImovelCreateDTO dto) {
        Imovel novoImovel = mapper.toEntity(dto);
        imovelDomainService.validarCriacao(novoImovel);
        return mapper.toResponse(imovelRepository.save(novoImovel));
    }

    @Transactional(readOnly = true)
    public List<ImovelResponseDTO> listarTodos() {
        return mapper.toResponseList(imovelRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ImovelResponseDTO buscarPorId(Long id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    @Transactional
    public ImovelResponseDTO atualizar(Long id, ImovelUpdateDTO dto) {
        Imovel imovel = buscarEntidade(id);
        imovelDomainService.validarAtualizacao(imovel, dto.codigo());
        imovel.atualizarDados(dto.codigo(), dto.titulo(), dto.cidade(), dto.preco());
        return mapper.toResponse(imovelRepository.save(imovel));
    }

    @Transactional
    public ImovelResponseDTO alterarStatus(Long id, ImovelPatchDTO dto) {
        Imovel imovel = buscarEntidade(id);
        imovelDomainService.validarMudancaStatus(imovel, dto.status().toDomain());
        imovel.alterarStatus(dto.status().toDomain());
        return mapper.toResponse(imovelRepository.save(imovel));
    }

    @Transactional
    public void remover(Long id) {
        imovelRepository.delete(buscarEntidade(id));
    }

    private Imovel buscarEntidade(Long id) {
        return imovelRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Imóvel não encontrado para o identificador: " + id));
    }
}
