package br.com.onwebtec.habitare.visita;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
interface VisitaRepository extends JpaRepository<Visita, Long> {
    List<Visita> findByUsuarioId(Long usuarioId);
    Optional<Visita> findByIdAndUsuarioId(Long id, Long usuarioId);
    boolean existsByImovelIdAndDataHora(Long imovelId, LocalDateTime dataHora);
}
