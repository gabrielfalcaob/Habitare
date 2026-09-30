package br.com.onwebtec.habitare.shared.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    //Erros de validação do Jakarta Bean Validation (@Valid).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarValidacao(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos estão inválidos. Corrija e tente novamente."
        );
        problem.setType(URI.create("about:blank"));
        problem.setTitle("Erro de validação de dados de entrada");
        problem.setProperty("timestamp", Instant.now());
        Map<String, String> camposComErro = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            camposComErro.put(fe.getField(), fe.getDefaultMessage());
        }
        problem.setProperty("erros", camposComErro);
        return problem;
    }
    //Erros de leitura do JSON (JSON malformado, tipo incompatível ou valor inválido para Enum).
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail tratarMensagemIlegivel(HttpMessageNotReadableException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "O corpo da requisição é inválido ou contém dados malformatados. Verifique tipos de dados e valores de enum."
        );
        problem.setType(URI.create("about:blank"));
        problem.setTitle("Corpo da requisição ilegível");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    // Violação de regra de negócio/duplicidade (HTTP 422 Unprocessable Entity)
    @ExceptionHandler(OperacaoInvalidaException.class)
    public ProblemDetail tratarOperacaoInvalida(OperacaoInvalidaException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage()
        );
        problem.setType(URI.create("about:blank"));
        problem.setTitle("Regra de negócio violada");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    // Duplicidade de recurso (HTTP 409 Conflict)
    @ExceptionHandler(ConflitoException.class)
    public ProblemDetail tratarConflito(ConflitoException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        problem.setType(URI.create("about:blank"));
        problem.setTitle("Conflito de dados");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    // Violação de integridade vinda do banco (ex.: unicidade), HTTP 409 Conflict
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarIntegridade(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade de dados: {}", ex.getMostSpecificCause().getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "Os dados informados conflitam com um registro já existente."
        );
        problem.setType(URI.create("about:blank"));
        problem.setTitle("Conflito de dados");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    // Recurso não encontrado (HTTP 404 Not Found)
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail tratarEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problem.setType(URI.create("about:blank"));
        problem.setTitle("Recurso não encontrado");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    // Handler coringa para quaisquer outras exceções derivadas de NegocioException
    @ExceptionHandler(NegocioException.class)
    public ProblemDetail tratarNegocioGenerico(NegocioException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        problem.setType(URI.create("about:blank"));
        problem.setTitle("Violação de regra de negócio");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    // Credenciais inválidas (HTTP 401 Unauthorized)
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail tratarAutenticacao(AuthenticationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Credenciais inválidas. Verifique e-mail e senha."
        );
        problem.setType(URI.create("about:blank"));
        problem.setTitle("Falha na autenticação");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    // Perfil sem permissão (HTTP 403 Forbidden)
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail tratarAcessoNegado(AccessDeniedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "Seu perfil não possui permissão para este recurso."
        );
        problem.setType(URI.create("about:blank"));
        problem.setTitle("Acesso negado");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    // Fallback: nunca expor stack trace ao cliente (HTTP 500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ProblemDetail tratarErroGenerico(Exception ex) {
        log.error("Erro inesperado no processamento da requisição", ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno inesperado. Contate o suporte."
        );
        problem.setType(URI.create("about:blank"));
        problem.setTitle("Erro interno do servidor");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
