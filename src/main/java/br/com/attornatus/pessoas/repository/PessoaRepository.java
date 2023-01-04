package br.com.attornatus.pessoas.repository;

import br.com.attornatus.pessoas.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
