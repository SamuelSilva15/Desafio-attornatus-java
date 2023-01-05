package br.com.attornatus.pessoas.service;


import br.com.attornatus.pessoas.exception.EnderecoNotFoundException;
import br.com.attornatus.pessoas.exception.PessoaNotFoundException;
import br.com.attornatus.pessoas.model.Endereco;
import br.com.attornatus.pessoas.model.Pessoa;
import br.com.attornatus.pessoas.repository.PessoaRepository;
import br.com.attornatus.pessoas.service.impl.PessoaServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PessoaServiceImplTest {

    @Mock
    private PessoaRepository pessoaRepository;

    private PessoaService pessoaService;


    @BeforeEach
    void setup() {
        pessoaService = new PessoaServiceImpl(pessoaRepository);
    }

    @Test
    void testeSalvaPessoa() {
        Pessoa pessoa = criaMockPessoa();
        when(pessoaRepository.save(pessoa)).thenReturn(pessoa);

        Pessoa pessoaSalva = pessoaService.save(pessoa);

        assertEquals(pessoa, pessoaSalva);
    }

    @Test
    void testeFindAll() {
        List<Pessoa> pessoas = Arrays.asList(criaMockPessoa(), criaMockPessoa());
        when(pessoaRepository.findAll()).thenReturn(pessoas);

        List<Pessoa> pessoasEncontradas = pessoaService.findAll();

        assertEquals(pessoas, pessoasEncontradas);
    }

    @Test
    void testeThrowPessoaNotFoundException() {
        Mockito.when(pessoaRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(PessoaNotFoundException.class, () -> pessoaService.findById(4L), "Pessoa não encontrada: 4L");
    }

    @Test
    void testeFindById() throws PessoaNotFoundException {
        Pessoa pessoa = criaMockPessoa();

        Mockito.when(pessoaRepository.findById(pessoa.getCodigo())).thenReturn(Optional.of(pessoa));

        Pessoa pessoa1 = pessoaService.findById(1L);

        assertNotNull(pessoa1);
        assertEquals(1L, pessoa1.getCodigo());
        assertEquals("Maria da Silva", pessoa1.getNome());
        assertEquals(LocalDate.of(2000, 1, 1), pessoa1.getDataNascimento());
        assertEquals(1, pessoa1.getEnderecos().get(0).getCodigo());
        assertEquals("Rua Soldado Floriano Peixoto", pessoa1.getEnderecos().get(0).getLogradouro());
        assertEquals(2, pessoa1.getEnderecos().get(1).getCodigo());
        assertEquals("Avenida Horiano Pedrosa", pessoa1.getEnderecos().get(1).getLogradouro());
    }

    @Test
    void testeUpdate() throws PessoaNotFoundException {
        Pessoa pessoa = criaMockPessoa();
        when(pessoaRepository.findById(pessoa.getCodigo())).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(pessoa)).thenReturn(pessoa);

        Pessoa pessoaAtualizada = pessoaService.update(pessoa.getCodigo(), pessoa);
        assertEquals(pessoa, pessoaAtualizada);
    }

    @Test
    void testeSalvaEnderecos() throws PessoaNotFoundException {
        Pessoa pessoa = criaMockPessoa();

        Mockito.when(pessoaRepository.findById(pessoa.getCodigo())).thenReturn(Optional.of(pessoa));

        List<Endereco> enderecosPessoa = pessoaService.saveEnderecos(pessoa.getCodigo(), enderecos());

        assertEquals(pessoa.getEnderecos(), enderecosPessoa);
    }

    @Test
    void testeListaEnderecos() throws PessoaNotFoundException {
        Pessoa pessoa = criaMockPessoa();

        Mockito.when(pessoaRepository.findById(pessoa.getCodigo())).thenReturn(Optional.of(pessoa));

        List<Endereco> enderecos = pessoaService.getEnderecos(1L);

        assertEquals(pessoa.getEnderecos(), enderecos);
    }

    @Test
    void testePrimaryAddress() throws PessoaNotFoundException, EnderecoNotFoundException {
        Pessoa pessoa = criaMockPessoa();

        Mockito.when(pessoaRepository.findById(pessoa.getCodigo())).thenReturn(Optional.of(pessoa));

        Endereco endereco = pessoaService.primaryAddress(1L, 1L);

        assertEquals(Boolean.TRUE, endereco.isPrimaryAddress());


    }



    private Pessoa criaMockPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(1L);
        pessoa.setNome("Maria da Silva");
        pessoa.setDataNascimento(LocalDate.of(2000, 1, 1));

        pessoa.setEnderecos(enderecos());

        return pessoa;
    }

    private List<Endereco> enderecos() {
        Endereco endereco1 = new Endereco();
        endereco1.setCodigo(1L);
        endereco1.setLogradouro("Rua Soldado Floriano Peixoto");
        endereco1.setCep(1234L);
        endereco1.setNumero(123L);
        endereco1.setCidade("Caçapava");

        Endereco endereco2 = new Endereco();
        endereco2.setCodigo(2L);
        endereco2.setLogradouro("Avenida Horiano Pedrosa");
        endereco2.setCep(5678L);
        endereco2.setNumero(869L);
        endereco2.setCidade("Taubaté");

        return new ArrayList<>(List.of(endereco1, endereco2));
    }
}