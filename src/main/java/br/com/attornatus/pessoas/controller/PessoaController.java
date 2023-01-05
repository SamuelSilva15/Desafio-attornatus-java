package br.com.attornatus.pessoas.controller;

import br.com.attornatus.pessoas.exception.PessoaNotFoundException;
import br.com.attornatus.pessoas.model.Pessoa;
import br.com.attornatus.pessoas.service.PessoaService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

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
}
