package br.ufrn.PDSgrupo5.framework.exception;

import org.springframework.validation.BindingResult;

/**
 * Classe para tratamento de exceção do tipo de validação de dados
 * @author Karina Maria e Maria Eduarda
 * @version 1.0.0
 */
public class ValidacaoException extends Exception{
    private static final long serialVersionUID = 1L;

    private BindingResult bindingResult;

    public ValidacaoException(BindingResult br){
        this.bindingResult = br;
    }

    public ValidacaoException(String message){
        super(message);
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
