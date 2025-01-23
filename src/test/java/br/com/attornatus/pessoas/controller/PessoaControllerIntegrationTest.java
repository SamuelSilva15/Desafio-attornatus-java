package br.com.attornatus.pessoas.controller;

import br.com.attornatus.pessoas.model.Endereco;
import br.com.attornatus.pessoas.model.Pessoa;
import br.com.attornatus.pessoas.repository.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PessoaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PessoaRepository pessoaRepository;

    @BeforeEach
    void setUp() {
        Endereco primeiroEndereco = new Endereco("logradouro", 12L, 12L, "city", true);
        Pessoa primeiraPessoa = new Pessoa("Samuel", LocalDate.now(), primeiroEndereco);
        pessoaRepository.save(primeiraPessoa);

        Endereco segundoEndereco = new Endereco("logradouro 2", 13L, 13L, "cidade", false);
        Pessoa segundaPessoa = new Pessoa("Araujo", LocalDate.now(), segundoEndereco);
        pessoaRepository.save(segundaPessoa);
    }

    @Test
    @Order(3)
    void save() throws Exception {
        Endereco endereco = new Endereco("RUA", 125409L, 245L, "Osasco", true);
        Pessoa pessoa = new Pessoa("Messi", LocalDate.now(), endereco);

        String pessoaJson = objectMapper.writeValueAsString(pessoa);
        String response = mockMvc.perform(post("/api/v1/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pessoaJson))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        Pessoa pessoaSalva = objectMapper.readValue(response, Pessoa.class);
        assertEquals(7L, pessoaSalva.getCodigo());
        assertEquals("Messi", pessoaSalva.getNome());
        assertEquals(LocalDate.now(), pessoaSalva.getDataNascimento());

        for (Endereco enderecoEncontrado : pessoaSalva.getEnderecos()) {
            assertEquals(7L, enderecoEncontrado.getCodigo());
            assertEquals(125409L, enderecoEncontrado.getCep());
            assertEquals("RUA", enderecoEncontrado.getLogradouro());
            assertEquals(245L, enderecoEncontrado.getNumero());
            assertEquals("Osasco", enderecoEncontrado.getCidade());
            assertTrue(true);
        }
    }

    @Test
    @Order(1)
    void findById() throws Exception {

        String response = mockMvc.perform(get("/api/v1/pessoas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pessoa pessoa = objectMapper.readValue(response, Pessoa.class);
        assertEquals(1L, pessoa.getCodigo());
        assertEquals("Samuel", pessoa.getNome());
        assertEquals(LocalDate.now(), pessoa.getDataNascimento());

        Endereco endereco = pessoa.getEnderecos().get(0);
        assertEquals("logradouro", endereco.getLogradouro());
        assertEquals(12L, endereco.getNumero());
        assertEquals("city", endereco.getCidade());
        assertTrue(endereco.isPrimaryAddress());
    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        String response = mockMvc.perform(get("/api/v1/pessoas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Pessoa> pessoas = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Pessoa.class));
        assertThat(!pessoas.isEmpty()).isTrue();
        Pessoa primeiraPessoaEncontrada = pessoas.get(0);
        assertEquals(1L, primeiraPessoaEncontrada.getCodigo());
        assertEquals("Samuel", primeiraPessoaEncontrada.getNome());
        assertEquals(LocalDate.now(), primeiraPessoaEncontrada.getDataNascimento());

        Endereco primeiroEnderecoEncontrado = primeiraPessoaEncontrada.getEnderecos().get(0);
        assertEquals("logradouro", primeiroEnderecoEncontrado.getLogradouro());
        assertEquals(12L, primeiroEnderecoEncontrado.getNumero());
        assertEquals("city", primeiroEnderecoEncontrado.getCidade());
        assertTrue(primeiroEnderecoEncontrado.isPrimaryAddress());

        Pessoa segundaPessoaEncontrada = pessoas.get(1);
        assertEquals(2L, segundaPessoaEncontrada.getCodigo());
        assertEquals("Araujo", segundaPessoaEncontrada.getNome());
        assertEquals(LocalDate.now(), segundaPessoaEncontrada.getDataNascimento());

        Endereco segundoEnderecoEncontrado = segundaPessoaEncontrada.getEnderecos().get(0);
        assertEquals("logradouro 2", segundoEnderecoEncontrado.getLogradouro());
        assertEquals(13L, segundoEnderecoEncontrado.getNumero());
        assertEquals("cidade", segundoEnderecoEncontrado.getCidade());
        assertFalse(segundoEnderecoEncontrado.isPrimaryAddress());
    }

    @Test
    @Order(4)
    void update() throws Exception {
        Endereco endereco = new Endereco("Nova rua", 1000L, 452L, "Nova cidade", false);
        Pessoa pessoaAtualizada = new Pessoa("William", LocalDate.now(), endereco);

        String pessoaAtualizadaJson = objectMapper.writeValueAsString(pessoaAtualizada);

        String response = mockMvc.perform(put("/api/v1/pessoas/{codigo}/", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pessoaAtualizadaJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(response);

        Pessoa pessoa = objectMapper.readValue(response, Pessoa.class);
        assertEquals(1L, pessoa.getCodigo());
        assertEquals("William", pessoa.getNome());
        assertEquals(LocalDate.now(), pessoa.getDataNascimento());
    }

    @Test
    @Order(5)
    void salvaEnderecos() throws Exception {
        Endereco endereco = new Endereco("logradouro 3", 16L, 16L, "cidade 2", false);
        List<Endereco> enderecos = List.of(endereco);
        String enderecosJson = objectMapper.writeValueAsString(enderecos);

        String response = mockMvc.perform(post("/api/v1/pessoas/{codigo}/enderecos", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enderecosJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Endereco> enderecosSalvos = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Endereco.class));
        System.out.println(enderecosSalvos);

        assertThat(!enderecosSalvos.isEmpty()).isTrue();

        Endereco primeiroEnderecoEncontrado = enderecosSalvos.get(0);
        assertEquals(10L, primeiroEnderecoEncontrado.getCodigo());
        assertEquals(452L, primeiroEnderecoEncontrado.getNumero());
        assertEquals("Nova rua", primeiroEnderecoEncontrado.getLogradouro());
        assertEquals(1000L, primeiroEnderecoEncontrado.getCep());
        assertEquals("Nova cidade", primeiroEnderecoEncontrado.getCidade());
        assertFalse(primeiroEnderecoEncontrado.isPrimaryAddress());

        Endereco segundoEnderecoEncontrado = enderecosSalvos.get(1);
        assertEquals(13L, segundoEnderecoEncontrado.getCodigo());
        assertEquals(16L, segundoEnderecoEncontrado.getNumero());
        assertEquals("logradouro 3", segundoEnderecoEncontrado.getLogradouro());
        assertEquals(16L, segundoEnderecoEncontrado.getNumero());
        assertEquals("cidade 2", segundoEnderecoEncontrado.getCidade());
        assertFalse(segundoEnderecoEncontrado.isPrimaryAddress());
    }

    @Test
    void getEnderecos() throws Exception {
        String response = mockMvc.perform(get("/api/v1/pessoas/{codigo}/enderecos", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Endereco> enderecos = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Endereco.class));

        Endereco primeiroEnderecoEncontrado = enderecos.get(0);
        assertEquals(10L, primeiroEnderecoEncontrado.getCodigo());
        assertEquals("Nova rua", primeiroEnderecoEncontrado.getLogradouro());
        assertEquals(452L, primeiroEnderecoEncontrado.getNumero());
        assertEquals(1000L, primeiroEnderecoEncontrado.getCep());
        assertEquals("Nova cidade", primeiroEnderecoEncontrado.getCidade());
        assertTrue(primeiroEnderecoEncontrado.isPrimaryAddress());

        Endereco segundoEnderecoEncontrado = enderecos.get(1);
        assertEquals(13L, segundoEnderecoEncontrado.getCodigo());
        assertEquals("logradouro 3", segundoEnderecoEncontrado.getLogradouro());
        assertEquals(16L, segundoEnderecoEncontrado.getNumero());
        assertEquals(16L, segundoEnderecoEncontrado.getCep());
        assertEquals("cidade 2", segundoEnderecoEncontrado.getCidade());
        assertFalse(segundoEnderecoEncontrado.isPrimaryAddress());
    }

    @Test
    @Order(6)
    void primaryAddress() throws Exception {
        String response = mockMvc.perform(patch("/api/v1/pessoas/{codigoPessoa}/enderecos/{codigoEndereco}/primary-address", 1L, 10L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(response);

        Endereco endereco = objectMapper.readValue(response, Endereco.class);
        assertTrue(endereco.isPrimaryAddress());
    }
}