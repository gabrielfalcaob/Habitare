package br.com.onwebtec.habitare.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
class AdminDevSeeder implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(AdminDevSeeder.class);
    private static final String EMAIL_ADMIN = "admin@imob.com";

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final String senhaAdmin;

    AdminDevSeeder(UsuarioRepository repository,
                   PasswordEncoder passwordEncoder,
                   @Value("${app.admin.password:admin123}") String senhaAdmin) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.senhaAdmin = senhaAdmin;
    }

    @Override
    public void run(String... args) {
        if (repository.findByEmail(EMAIL_ADMIN).isPresent()) {
            return;
        }
        repository.save(new Usuario(EMAIL_ADMIN,
                passwordEncoder.encode(senhaAdmin), UserRole.ADMIN));
        log.info("Usuário administrador de desenvolvimento criado: {}", EMAIL_ADMIN);
    }
}
