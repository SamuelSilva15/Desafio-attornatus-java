package br.com.attornatus.pessoas.controller;

import br.com.attornatus.pessoas.model.Pessoa;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void testeSalvaPessoaAndProcuraPorCodigo() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders
                        .post(new URI("/api/v1/pessoas"))
                        .content(getPessoa())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");
        Pessoa pessoa = objectMapper.readValue(response.getContentAsString(), Pessoa.class);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(new URI("/api/v1/pessoas/" + pessoa.getCodigo())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(response.getContentAsString()));
    }

    @Test
    void testeListaTodasAsPessoas() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(new URI("/api/v1/pessoas"))
                        .content(getPessoa())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(new URI("/api/v1/pessoas"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    void testeAtualizacaoDeDadosDeUmaPessoa() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders
                        .post(new URI("/api/v1/pessoas"))
                        .content(getPessoa())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");
        Pessoa pessoa = objectMapper.readValue(response.getContentAsString(), Pessoa.class);


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
                        .put(new URI("/api/v1/pessoas/" + pessoa.getCodigo()))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());


        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(new URI("/api/v1/pessoas/" + pessoa.getCodigo())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(json));
    }

    @Test
    void testesSalvarNovoEnderecoAndListarEnderecos() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders
                        .post(new URI("/api/v1/pessoas"))
                        .content(getPessoa())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");
        Pessoa pessoa = objectMapper.readValue(response.getContentAsString(), Pessoa.class);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(new URI("/api/v1/pessoas/" + pessoa.getCodigo() +"/enderecos"))
                        .content(getEnderecos())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(new URI("/api/v1/pessoas/" + pessoa.getCodigo() + "/enderecos")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)));
    }

    @Test
    void testeEnderecoPrincipal() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders
                        .post(new URI("/api/v1/pessoas"))
                        .content(getPessoa())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");
        Pessoa pessoa = objectMapper.readValue(response.getContentAsString(), Pessoa.class);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .patch(new URI("/api/v1/pessoas/" + pessoa.getCodigo() + "/enderecos/" + pessoa.getEnderecos().get(0).getCodigo() + "/primary-address"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.primaryAddress", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.codigo", is(pessoa.getEnderecos().get(0).getCodigo().intValue())));
    }

    private static String getPessoa() {
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
