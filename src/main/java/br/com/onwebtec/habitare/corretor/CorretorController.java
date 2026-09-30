package br.com.onwebtec.habitare.corretor;

import br.com.onwebtec.habitare.corretor.dto.CorretorCreateDTO;
import br.com.onwebtec.habitare.corretor.dto.CorretorResponseDTO;
import br.com.onwebtec.habitare.corretor.dto.CorretorUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/corretores")
@Validated
public class CorretorController {
    private final CorretorApplicationService service;

    public CorretorController(CorretorApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CorretorResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{corretorId}")
    public ResponseEntity<CorretorResponseDTO> buscarPorId(@PathVariable Long corretorId) {
        return ResponseEntity.ok(service.buscarPorId(corretorId));
    }

    @PostMapping
    public ResponseEntity<CorretorResponseDTO> criar(@RequestBody @Valid CorretorCreateDTO dto,
                                                     UriComponentsBuilder uriBuilder) {
        CorretorResponseDTO salvo = service.criarCorretor(dto);
        URI uri = uriBuilder.path("/api/corretores/{corretorId}")
                .buildAndExpand(salvo.id())
                .toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @PutMapping("/{corretorId}")
    public ResponseEntity<CorretorResponseDTO> atualizar(@PathVariable Long corretorId,
                                                         @RequestBody @Valid CorretorUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizar(corretorId, dto));
    }

    @DeleteMapping("/{corretorId}")
    public ResponseEntity<Void> remover(@PathVariable Long corretorId) {
        service.remover(corretorId);
        return ResponseEntity.noContent().build();
    }
}
