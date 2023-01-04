package br.com.attornatus.pessoas.service;


import br.com.attornatus.pessoas.model.Endereco;
import br.com.attornatus.pessoas.model.Pessoa;
import br.com.attornatus.pessoas.repository.PessoaRepository;
import br.com.attornatus.pessoas.service.impl.PessoaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    private PessoaService pessoaService;


    @BeforeEach
    void setup() {
        pessoaService = new PessoaServiceImpl(pessoaRepository);
    }

    @Test
    public void testSalvaPessoa() {
        Pessoa pessoa = criaMockPessoa();
        when(pessoaRepository.save(pessoa)).thenReturn(pessoa);

        Pessoa pessoaSalva = pessoaService.save(pessoa);

        assertEquals(pessoa, pessoaSalva);
    }

    @Test
    public void testFindAll() {
        List<Pessoa> pessoas = Arrays.asList(criaMockPessoa(), criaMockPessoa());
        when(pessoaRepository.findAll()).thenReturn(pessoas);

        List<Pessoa> pessoasEncontradas = pessoaService.findAll();

        assertEquals(pessoas, pessoasEncontradas);
    }

    


    private Pessoa criaMockPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(1L);
        pessoa.setNome("Maria da Silva");
        pessoa.setDataNascimento(LocalDate.of(2000, 1, 1));

        Endereco endereco1 = new Endereco();
        endereco1.setCodigo(1L);
        endereco1.setLogradouro("Rua soldado floriano peixoto");
        endereco1.setCep(1234L);
        endereco1.setNumero(123L);
        endereco1.setCidade("Caçapava");

        Endereco endereco2 = new Endereco();
        endereco2.setCodigo(2L);
        endereco2.setLogradouro("Avenida horiano pedrosa");
        endereco2.setCep(5678L);
        endereco2.setNumero(869L);
        endereco2.setCidade("Taubaté");

        pessoa.setEnderecos(List.of(endereco1, endereco2));

        return pessoa;
    }
}