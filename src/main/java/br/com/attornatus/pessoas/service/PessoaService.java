package br.com.attornatus.pessoas.service;

import br.com.attornatus.pessoas.exception.EnderecoNotFoundException;
import br.com.attornatus.pessoas.exception.PessoaNotFoundException;
import br.com.attornatus.pessoas.model.Endereco;
import br.com.attornatus.pessoas.model.Pessoa;

import java.util.List;

public interface PessoaService {
    Pessoa save(Pessoa pessoa);

    List<Pessoa> findAll();

    Pessoa findById(Long codigo) throws PessoaNotFoundException;

    Pessoa update(Long codigo, Pessoa pessoa) throws PessoaNotFoundException;

    public List<Endereco> saveEnderecos(Long codigo, List<Endereco> endereco) throws PessoaNotFoundException;
    public List<Endereco> getEnderecos(Long codigo) throws PessoaNotFoundException;

    public Endereco primaryAddress(Long codigoPessoa, Long codigoEndereco) throws PessoaNotFoundException, EnderecoNotFoundException;
}
