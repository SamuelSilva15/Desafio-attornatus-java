package br.com.attornatus.pessoas.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testeSalvaPessoa() throws Exception {
        String json = "{\"nome\":\"Samuel Silva\",\"dataNascimento\":\"12/02/2001\"}";
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(new URI("/api/v1/pessoas"))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(new URI("/api/v1/pessoas/1")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"codigo\": 1, \"nome\":\"Samuel Silva\",\"dataNascimento\":\"12/02/2001\"}"));
    }
}
