package br.com.attornatus.pessoas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PessoaNotFoundException extends Exception {

    public PessoaNotFoundException(Long codigo) {
        super("Pessoa não encontrada: " + codigo);
    }
}
