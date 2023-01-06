package br.com.attornatus.pessoas.annotation;

import br.com.attornatus.pessoas.model.Endereco;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

public class JustOnePrimaryValidator implements ConstraintValidator<JustOnePrimary, List<Endereco>> {

    @Override
    public boolean isValid(List<Endereco> enderecos, ConstraintValidatorContext constraintValidatorContext) {
        if(Objects.isNull(enderecos) || enderecos.isEmpty()) {
            return true;
        }

        return enderecos.stream()
                .filter(Endereco::isPrimaryAddress)
                .count() < 2;
    }
}
