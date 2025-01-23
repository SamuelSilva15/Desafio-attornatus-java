package br.com.attornatus.pessoas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endereco implements Comparable<Endereco> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private String logradouro;
    private Long cep;
    private Long numero;
    private String cidade;
    private boolean primaryAddress;


    public Endereco(String logradouro, Long cep, Long numero, String cidade, boolean primaryAddress) {
        this.logradouro = logradouro;
        this.cep = cep;
        this.numero = numero;
        this.cidade = cidade;
        this.primaryAddress = primaryAddress;
    }

    @Override
    public int compareTo(Endereco o) {
        return Boolean.compare(o.isPrimaryAddress(), this.isPrimaryAddress());
    }
}
