package br.com.attornatus.pessoas.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Pessoa {


    private Long codigo;
    private String nome;


    private LocalDate dataNascimento;


    private List<Endereco> enderecos;

}
