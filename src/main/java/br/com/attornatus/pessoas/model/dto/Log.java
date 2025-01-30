package br.com.attornatus.pessoas.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Log {
    private String nome;
    private String level;
    private String type;
    private String message;
    private Date dataCadastro;
    private Origem origem;
}
