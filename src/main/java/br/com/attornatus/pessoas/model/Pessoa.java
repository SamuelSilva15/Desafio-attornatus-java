package br.com.attornatus.pessoas.model;

import br.com.attornatus.pessoas.annotation.JustOnePrimary;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "nameAttributeInThisClassWithOneToMany")
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    private String nome;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @JustOnePrimary(message = "Somente um endereço pode ser escolhido como o principal")
    @NotEmpty(message = "Ao menos um endereço deve ser salvo")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pessoa_codigo")
    private List<Endereco> enderecos;

    public List<Endereco> getEnderecos() {
        Collections.sort(enderecos);
        return enderecos;
    }
}
