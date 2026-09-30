package br.com.onwebtec.habitare.corretor;

import br.com.onwebtec.habitare.shared.exception.ConflitoException;
import org.springframework.stereotype.Service;

@Service
class CorretorService {
    private final CorretorRepository repository;

    CorretorService(CorretorRepository repository) {
        this.repository = repository;
    }

    public void validarCriacao(Corretor novoCorretor) {
        if (repository.existsByCreci(novoCorretor.getCreci())) {
            throw new ConflitoException("Já existe corretor cadastrado com o CRECI: "
                    + novoCorretor.getCreci());
        }
        if (repository.existsByEmail(novoCorretor.getEmail())) {
            throw new ConflitoException("Já existe corretor cadastrado com o e-mail: "
                    + novoCorretor.getEmail());
        }
    }

    public void validarAtualizacao(Corretor corretor, String novoCreci, String novoEmail) {
        if (repository.existsByCreciAndIdNot(novoCreci, corretor.getId())) {
            throw new ConflitoException("Já existe corretor cadastrado com o CRECI: " + novoCreci);
        }
        if (repository.existsByEmailAndIdNot(novoEmail, corretor.getId())) {
            throw new ConflitoException("Já existe corretor cadastrado com o e-mail: " + novoEmail);
        }
    }
}
