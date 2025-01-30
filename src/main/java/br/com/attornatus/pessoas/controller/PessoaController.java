package br.com.attornatus.pessoas.controller;

import br.com.attornatus.pessoas.annotation.JustOnePrimary;
import br.com.attornatus.pessoas.exception.EnderecoNotFoundException;
import br.com.attornatus.pessoas.exception.PessoaNotFoundException;
import br.com.attornatus.pessoas.model.Endereco;
import br.com.attornatus.pessoas.model.Pessoa;
import br.com.attornatus.pessoas.model.dto.Log;
import br.com.attornatus.pessoas.model.dto.Origem;
import br.com.attornatus.pessoas.service.PessoaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value="api/v1/pessoas")
public class PessoaController {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PessoaService pessoaService;

    @PostMapping
    public Pessoa save(@Valid @RequestBody Pessoa pessoa) {
        return pessoaService.save(pessoa);
    }

    @GetMapping("/{codigo}")
    public Pessoa findById(@PathVariable Long codigo) throws PessoaNotFoundException, JsonProcessingException {
        try {
            return pessoaService.findById(codigo);
        } catch (PessoaNotFoundException e) {
            Origem origem = Origem.builder()
                    .nome("Attornatus").cnpj("12345678998765").build();

            Log log = Log.builder()
                    .level("2")
                    .type("ERRO")
                    .message(e.getMessage())
                    .origem(origem)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            String logJson = objectMapper.writeValueAsString(log);

            kafkaTemplate.send("log-producer-topico", logJson);
            throw new PessoaNotFoundException(codigo);
        }
    }

    @GetMapping
    public List<Pessoa> findAll() {
        return pessoaService.findAll();
    }

    @PutMapping("/{codigo}")
    public Pessoa update(@PathVariable Long codigo, @RequestBody @Valid Pessoa pessoa) throws PessoaNotFoundException {
        try {
            return pessoaService.update(codigo, pessoa);
        } catch (PessoaNotFoundException e) {
            throw new PessoaNotFoundException(codigo);
        }
    }

    @PostMapping("/{codigo}/enderecos")
    public List<Endereco> salvaEnderecos(@PathVariable Long codigo, @RequestBody @Valid @JustOnePrimary List<Endereco> enderecos) throws PessoaNotFoundException {
        try {
            return pessoaService.saveEnderecos(codigo, enderecos);
        } catch (PessoaNotFoundException e) {
            throw new PessoaNotFoundException(codigo);
        }
    }

    @GetMapping("/{codigo}/enderecos")
    public List<Endereco> getEnderecos(@PathVariable Long codigo) throws PessoaNotFoundException {
        try {
            return pessoaService.getEnderecos(codigo);
        } catch (PessoaNotFoundException e) {
            throw new PessoaNotFoundException(codigo);
        }
    }

    @PatchMapping("/{codigoPessoa}/enderecos/{codigoEndereco}/primary-address")
    public Endereco primaryAddress(@PathVariable Long codigoPessoa, @PathVariable Long codigoEndereco) throws PessoaNotFoundException, EnderecoNotFoundException {
        try {
            return pessoaService.primaryAddress(codigoPessoa, codigoEndereco);
        } catch (PessoaNotFoundException e) {
            throw new PessoaNotFoundException(codigoPessoa);
        }
    }
}
