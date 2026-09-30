package br.com.onwebtec.habitare.imovel;

import br.com.onwebtec.habitare.shared.exception.ConflitoException;
import br.com.onwebtec.habitare.shared.exception.OperacaoInvalidaException;
import org.springframework.stereotype.Service;

@Service
class ImovelService {
    private final ImovelRepository repository;

    ImovelService(ImovelRepository repository) {
        this.repository = repository;
    }

    public void validarCriacao(Imovel novoImovel) {
        if (repository.existsByCodigo(novoImovel.getCodigo())) {
            throw new ConflitoException("Já existe imóvel cadastrado com o código: "
                    + novoImovel.getCodigo());
        }
    }

    public void validarAtualizacao(Imovel imovel, String novoCodigo) {
        if (repository.existsByCodigoAndIdNot(novoCodigo, imovel.getId())) {
            throw new ConflitoException("Já existe imóvel cadastrado com o código: " + novoCodigo);
        }
    }

    public void validarMudancaStatus(Imovel imovel, ImovelStatus novoStatus) {
        if (imovel.getStatus() == ImovelStatus.VENDIDO && novoStatus != ImovelStatus.VENDIDO) {
            throw new OperacaoInvalidaException(
                    "Um imóvel VENDIDO não pode retornar para o status " + novoStatus + ".");
        }
    }
}
