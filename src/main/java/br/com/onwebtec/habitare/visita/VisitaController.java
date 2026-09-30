package br.com.onwebtec.habitare.visita;

import br.com.onwebtec.habitare.auth.Usuario;
import br.com.onwebtec.habitare.visita.dto.VisitaCreateDTO;
import br.com.onwebtec.habitare.visita.dto.VisitaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/visitas")
@Validated
public class VisitaController {
    private final VisitaApplicationService service;

    public VisitaController(VisitaApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<VisitaResponseDTO>> listar(
            @AuthenticationPrincipal Usuario usuarioAutenticado) {
        return ResponseEntity.ok(service.listarDoUsuario(usuarioAutenticado.getId()));
    }

    @PostMapping
    public ResponseEntity<VisitaResponseDTO> agendar(
            @AuthenticationPrincipal Usuario usuarioAutenticado,
            @RequestBody @Valid VisitaCreateDTO dto,
            UriComponentsBuilder uriBuilder) {
        VisitaResponseDTO salva = service.agendar(dto, usuarioAutenticado.getId());
        URI uri = uriBuilder.path("/api/visitas/{visitaId}")
                .buildAndExpand(salva.id())
                .toUri();
        return ResponseEntity.created(uri).body(salva);
    }

    @DeleteMapping("/{visitaId}")
    public ResponseEntity<Void> cancelar(@AuthenticationPrincipal Usuario usuarioAutenticado,
                                         @PathVariable Long visitaId) {
        service.cancelar(visitaId, usuarioAutenticado.getId());
        return ResponseEntity.noContent().build();
    }
}
