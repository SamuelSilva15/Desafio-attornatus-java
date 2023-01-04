package br.com.attornatus.pessoas.service.impl;

import br.com.attornatus.pessoas.model.Pessoa;
import br.com.attornatus.pessoas.repository.PessoaRepository;
import br.com.attornatus.pessoas.service.PessoaService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class PessoaServiceImpl implements PessoaService {

    private final PessoaRepository pessoaRepository;

    @Override
    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }
}