package br.com.attornatus.pessoas.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.swing.text.EditorKit;
import java.util.Comparator;

@Entity
@Data
public class Endereco implements Comparable<Endereco> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private String logradouro;
    private Long cep;
    private Long numero;

    private String cidade;

    private boolean primaryAddress;

    @Override
    public int compareTo(Endereco o) {
        return Boolean.compare(o.isPrimaryAddress(), this.isPrimaryAddress());
    }
}
