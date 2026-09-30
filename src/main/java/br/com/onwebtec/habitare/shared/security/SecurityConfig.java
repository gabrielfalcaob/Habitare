package br.com.onwebtec.habitare.shared.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                escreverProblem(res, HttpServletResponse.SC_UNAUTHORIZED,
                                        "Autenticação necessária",
                                        "Informe um token JWT válido no cabeçalho Authorization."))
                        .accessDeniedHandler((req, res, e) ->
                                escreverProblem(res, HttpServletResponse.SC_FORBIDDEN,
                                        "Acesso negado",
                                        "Seu perfil não possui permissão para este recurso.")))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/api/imoveis", "/api/imoveis/**").permitAll();
                    req.requestMatchers("/api/imoveis/**").hasRole("ADMIN");
                    req.requestMatchers("/api/visitas/**").authenticated();
                    req.requestMatchers(HttpMethod.POST, "/api/corretores/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/api/corretores/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/api/corretores/**").hasRole("ADMIN");
                    req.requestMatchers("/api/corretores/**").authenticated();
                    req.anyRequest().authenticated();})
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private static void escreverProblem(HttpServletResponse response, int status,
                                        String titulo, String detalhe) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.valueOf(status), detalhe);
        problem.setTitle(titulo);
        String json = "{\"status\":%d,\"title\":\"%s\",\"detail\":\"%s\"}"
                .formatted(status, titulo, detalhe);
        response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
