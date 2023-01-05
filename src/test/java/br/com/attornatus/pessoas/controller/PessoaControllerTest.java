package br.com.attornatus.pessoas.controller;

import br.com.attornatus.pessoas.model.Endereco;
import br.com.attornatus.pessoas.model.Pessoa;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testeSalvaPessoaAndProcuraPorCodigo() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(new URI("/api/v1/pessoas"))
                        .content(getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));

        /* --> TesteFindById <-- */
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(new URI("/api/v1/pessoas/1")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andExpect(MockMvcResultMatchers.content()
                        .json(getJson()));
    }

    @Test
    void testeListaTodasAsPessoas() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(new URI("/api/v1/pessoas"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    void testeAtualizacaoDeDadosDeUmaPessoa() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(new URI("/api/v1/pessoas"))
                        .content(getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));


        String json = "{\n" +
                "    \"nome\": \"Samuel Silva\",\n" +
                "    \"dataNascimento\": \"13/05/2008\",\n" +
                "    \"enderecos\": [\n" +
                "        {\n" +
                "            \"logradouro\": \"Avenidade José Rufino César Guimarães\",\n" +
                "            \"cep\": 12283260,\n" +
                "            \"numero\": 275,\n" +
                "            \"cidade\": \"Caçapava\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        mockMvc
                .perform(MockMvcRequestBuilders
                        .put(new URI("/api/v1/pessoas/1"))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));


        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(new URI("/api/v1/pessoas/1")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andExpect(MockMvcResultMatchers.content()
                        .json(json));
    }

    @Test
    void testesSalvarNovoEnderecoAndListarEnderecos() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(new URI("/api/v1/pessoas"))
                        .content(getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(new URI("/api/v1/pessoas/1/enderecos"))
                        .content(getEnderecos())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));

        /* --> TestesListaDeEnderecos <-- */
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(new URI("/api/v1/pessoas/1/enderecos")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)));
    }

    private static String getJson() {
        return "{\n" +
                "    \"nome\": \"Samuel Araújo da Silva\",\n" +
                "    \"dataNascimento\": \"12/01/2001\", \n" +
                "    \"enderecos\": [{\n" +
                "        \"logradouro\": \"Avenidade José Rufino César Guimarães\",\n" +
                "        \"cep\": 12283260,\n" +
                "        \"numero\": 275,\n" +
                "        \"cidade\": \"Caçapava\"\n" +
                "    },\n" +


                "    {\n" +
                "        \"logradouro\": \"Rua Soldado Machado de Assis\",\n" +
                "        \"cep\": 35252523,\n" +
                "        \"numero\": 665,\n" +
                "        \"cidade\": \"Taubaté\"\n" +
                "    }]\n" +
                "   \n" +
                "}";
    }

    private static String getEnderecos() {
        return "[{\n" +
                "        \"logradouro\": \"Wagner Moura Castanho\",\n" +
                "        \"cep\": 7882828,\n" +
                "        \"numero\": 572,\n" +
                "        \"cidade\": \"São José dos Campos\",\n" +
                "        \"primaryAddress\": true\n" +
                "    },\n" +


                "    {\n" +
                "        \"logradouro\": \"Soldado floriano peixoto\",\n" +
                "        \"cep\": \"35252523\",\n" +
                "        \"numero\": 778,\n" +
                "        \"cidade\": \"Taubaté\"\n" +
                "    }\n" +
                "]   \n";
    }

    private static String todosEnderecos() {
        return "[{\n" +
                "        \"logradouro\": \"Wagner Moura Castanho\",\n" +
                "        \"cep\": 7882828,\n" +
                "        \"numero\": 572,\n" +
                "        \"cidade\": \"São José dos Campos\",\n" +
                "        \"primaryAddress\": true\n" +
                "    },\n" +
                "    {\n" +
                "        \"logradouro\": \"Soldado floriano peixoto\",\n" +
                "        \"cep\": \"35252523\",\n" +
                "        \"numero\": 778,\n" +
                "        \"cidade\": \"Taubaté\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"logradouro\": \"Avenidade José Rufino César Guimarães\",\n" +
                "        \"cep\": 12283260,\n" +
                "        \"numero\": 275,\n" +
                "        \"cidade\": \"Caçapava\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"logradouro\": \"Rua soldado machado de assis\",\n" +
                "        \"cep\": 563535,\n" +
                "        \"numero\": 665,\n" +
                "        \"cidade\": \"Taubaté\"\n" +
                "    }\n" +
                "] ";
    }
}
