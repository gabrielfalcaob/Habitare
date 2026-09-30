package br.com.onwebtec.habitare.imovel;

import br.com.onwebtec.habitare.imovel.dto.ImovelCreateDTO;
import br.com.onwebtec.habitare.imovel.dto.ImovelPatchDTO;
import br.com.onwebtec.habitare.imovel.dto.ImovelResponseDTO;
import br.com.onwebtec.habitare.imovel.dto.ImovelUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/imoveis")
@Validated
public class ImovelController {
    private final ImovelApplicationService service;

    public ImovelController(ImovelApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ImovelResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{imovelId}")
    public ResponseEntity<ImovelResponseDTO> buscarPorId(@PathVariable Long imovelId) {
        return ResponseEntity.ok(service.buscarPorId(imovelId));
    }

    @PostMapping
    public ResponseEntity<ImovelResponseDTO> criar(@RequestBody @Valid ImovelCreateDTO dto,
                                                   UriComponentsBuilder uriBuilder) {
        ImovelResponseDTO salvo = service.criarImovel(dto);
        URI uri = uriBuilder.path("/api/imoveis/{imovelId}")
                .buildAndExpand(salvo.id())
                .toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @PutMapping("/{imovelId}")
    public ResponseEntity<ImovelResponseDTO> atualizar(@PathVariable Long imovelId,
                                                       @RequestBody @Valid ImovelUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizar(imovelId, dto));
    }

    @PatchMapping("/{imovelId}")
    public ResponseEntity<ImovelResponseDTO> alterarStatus(@PathVariable Long imovelId,
                                                           @RequestBody @Valid ImovelPatchDTO dto) {
        return ResponseEntity.ok(service.alterarStatus(imovelId, dto));
    }

    @DeleteMapping("/{imovelId}")
    public ResponseEntity<Void> remover(@PathVariable Long imovelId) {
        service.remover(imovelId);
        return ResponseEntity.noContent().build();
    }
}
