package br.com.attornatus.pessoas.exception;

public class EnderecoNotFoundException extends Exception {

    public EnderecoNotFoundException(Long codigo) {
        super("Endereço não encontrado: " + codigo);
    }
}
