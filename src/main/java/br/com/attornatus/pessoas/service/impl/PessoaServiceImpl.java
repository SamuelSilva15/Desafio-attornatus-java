package br.com.attornatus.pessoas.service.impl;

import br.com.attornatus.pessoas.exception.EnderecoNotFoundException;
import br.com.attornatus.pessoas.exception.PessoaNotFoundException;
import br.com.attornatus.pessoas.model.Endereco;
import br.com.attornatus.pessoas.model.Pessoa;
import br.com.attornatus.pessoas.repository.PessoaRepository;
import br.com.attornatus.pessoas.service.PessoaService;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class PessoaServiceImpl implements PessoaService {

    private final PessoaRepository pessoaRepository;

    @Override
    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    @Override
    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    @Override
    public Pessoa findById(Long codigo) throws PessoaNotFoundException {
        return pessoaRepository.findById(codigo)
                .orElseThrow(() -> new PessoaNotFoundException(codigo));
    }

    @Override
    public Pessoa update(Long codigo, Pessoa pessoa) throws PessoaNotFoundException {
        Pessoa pessoaAtual = pessoaRepository.findById(codigo)
                .orElseThrow(() -> new PessoaNotFoundException(codigo));

        pessoaAtual.setNome(pessoa.getNome());
        pessoaAtual.setDataNascimento(pessoa.getDataNascimento());
        pessoaAtual.setEnderecos(pessoa.getEnderecos());

        return pessoaRepository.save(pessoaAtual);
    }

    @Override
    public List<Endereco> saveEnderecos(Long codigo, List<Endereco> endereco) throws PessoaNotFoundException {
        Pessoa pessoa = pessoaRepository.findById(codigo)
                .orElseThrow(() -> new PessoaNotFoundException(codigo));

        List<Endereco> enderecosAtuais = pessoa.getEnderecos();
        enderecosAtuais.addAll(endereco);
        pessoa.setEnderecos(enderecosAtuais);

        pessoaRepository.save(pessoa);

        return enderecosAtuais;
    }

    @Override
    public List<Endereco> getEnderecos(Long codigo) throws PessoaNotFoundException {
        Pessoa pessoa = pessoaRepository.findById(codigo)
                .orElseThrow(() -> new PessoaNotFoundException(codigo));

        return pessoa.getEnderecos();
    }

    @Override
    public Endereco primaryAddress(Long codigoPessoa, Long codigoEndereco) throws PessoaNotFoundException, EnderecoNotFoundException {
        Pessoa pessoa = pessoaRepository.findById(codigoPessoa).orElseThrow(() -> new PessoaNotFoundException(codigoPessoa));
        Endereco endereco = pessoa.getEnderecos().stream()
                .filter(end -> end.getCodigo() == codigoEndereco)
                .findFirst()
                .orElseThrow(() -> new EnderecoNotFoundException(codigoEndereco));

        endereco.setPrimaryAddress(Boolean.TRUE);
        pessoaRepository.save(pessoa);

        return endereco;
    }
}
