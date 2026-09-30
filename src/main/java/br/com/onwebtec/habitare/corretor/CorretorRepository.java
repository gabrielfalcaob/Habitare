package br.com.onwebtec.habitare.corretor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CorretorRepository extends JpaRepository<Corretor, Long> {
    boolean existsByCreci(String creci);
    boolean existsByEmail(String email);
    boolean existsByCreciAndIdNot(String creci, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
}
