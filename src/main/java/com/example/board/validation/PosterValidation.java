package com.example.board.validation;

import com.example.board.domain.Poster;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PosterValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Poster.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Poster poster = (Poster) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "require");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "writer", "require");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "require");

        if(poster.getHeight()==null || poster.getHeight()<100)
            errors.rejectValue("height", "min", new Object[]{100}, null);

        if(poster.getWeight()==null || poster.getWeight()<40)
            errors.rejectValue("weight", "min", new Object[]{40}, null);

        if(poster.getHeight()!=null && poster.getWeight()!=null){
            Long height = poster.getHeight();
            Long weight = poster.getWeight();

            if(height+weight<150)
                errors.reject("heightWeightSumMin", new Object[]{150, height+weight}, null);
        }

    }

}
