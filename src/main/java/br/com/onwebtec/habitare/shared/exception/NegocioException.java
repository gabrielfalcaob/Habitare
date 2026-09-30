package br.com.onwebtec.habitare.shared.exception;

public abstract class NegocioException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    protected NegocioException(String mensagem) {
        super(mensagem);}
}
