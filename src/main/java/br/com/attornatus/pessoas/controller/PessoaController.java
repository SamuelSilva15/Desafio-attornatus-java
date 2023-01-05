package br.com.attornatus.pessoas.controller;

import br.com.attornatus.pessoas.exception.EnderecoNotFoundException;
import br.com.attornatus.pessoas.exception.PessoaNotFoundException;
import br.com.attornatus.pessoas.model.Endereco;
import br.com.attornatus.pessoas.model.Pessoa;
import br.com.attornatus.pessoas.service.PessoaService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Data
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value="api/v1/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    @PostMapping
    public Pessoa save(@RequestBody Pessoa pessoa) {
        return pessoaService.save(pessoa);
    }

    @GetMapping("/{codigo}")
    public Pessoa findById(@PathVariable Long codigo) throws PessoaNotFoundException {
        return pessoaService.findById(codigo);
    }

    @GetMapping
    public List<Pessoa> findAll() {
        return pessoaService.findAll();
    }

    @PutMapping("/{codigo}")
    public Pessoa update(@PathVariable Long codigo, @RequestBody @Valid Pessoa pessoa) throws PessoaNotFoundException {
        return pessoaService.update(codigo, pessoa);
    }
}
