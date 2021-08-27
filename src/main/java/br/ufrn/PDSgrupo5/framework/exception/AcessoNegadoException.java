package br.ufrn.PDSgrupo5.framework.exception;

/**
 * Classe para tratamento de exceção que verifica a permissão de acesso
 * @author Karina Maria e Maria Eduarda
 * @version 1.0.0
 */
public class AcessoNegadoException extends Exception{
    private static final long serialVersionUID = 1L;

    public AcessoNegadoException(String message) {
        super(message);
    }

}
