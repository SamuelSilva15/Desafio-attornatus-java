package br.com.attornatus.pessoas.service;

import br.com.attornatus.pessoas.model.Pessoa;

import java.util.List;

public interface PessoaService {
    Pessoa save(Pessoa pessoa);

    List<Pessoa> findAll();
}
