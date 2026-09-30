package br.com.onwebtec.habitare.imovel;

import br.com.onwebtec.habitare.imovel.dto.ImovelResumoDTO;

public interface ImovelInternalApi {
    ImovelResumoDTO buscarResumo(Long id);
}
