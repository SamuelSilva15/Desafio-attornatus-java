package br.com.attornatus.pessoas.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Origem {
    private String nome;
    private String cnpj;
}
