package br.com.onwebtec.habitare.corretor;

import br.com.onwebtec.habitare.corretor.dto.CorretorCreateDTO;
import br.com.onwebtec.habitare.corretor.dto.CorretorResponseDTO;
import br.com.onwebtec.habitare.corretor.dto.CorretorUpdateDTO;
import br.com.onwebtec.habitare.shared.exception.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class CorretorApplicationService {
    private final CorretorService corretorDomainService;
    private final CorretorRepository corretorRepository;
    private final CorretorMapper mapper;

    CorretorApplicationService(CorretorService corretorDomainService,
                               CorretorRepository corretorRepository,
                               CorretorMapper mapper) {
        this.corretorDomainService = corretorDomainService;
        this.corretorRepository = corretorRepository;
        this.mapper = mapper;
    }

    @Transactional
    public CorretorResponseDTO criarCorretor(CorretorCreateDTO dto) {
        Corretor novoCorretor = mapper.toEntity(dto);
        corretorDomainService.validarCriacao(novoCorretor);
        return mapper.toResponse(corretorRepository.save(novoCorretor));
    }

    @Transactional(readOnly = true)
    public List<CorretorResponseDTO> listarTodos() {
        return mapper.toResponseList(corretorRepository.findAll());
    }

    @Transactional(readOnly = true)
    public CorretorResponseDTO buscarPorId(Long id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    @Transactional
    public CorretorResponseDTO atualizar(Long id, CorretorUpdateDTO dto) {
        Corretor corretor = buscarEntidade(id);
        corretorDomainService.validarAtualizacao(corretor, dto.creci(), dto.email());
        corretor.atualizarDados(dto.nome(), dto.creci(), dto.telefone(), dto.email());
        return mapper.toResponse(corretorRepository.save(corretor));
    }

    @Transactional
    public void remover(Long id) {
        corretorRepository.delete(buscarEntidade(id));
    }

    private Corretor buscarEntidade(Long id) {
        return corretorRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Corretor não encontrado para o identificador: " + id));
    }
}
