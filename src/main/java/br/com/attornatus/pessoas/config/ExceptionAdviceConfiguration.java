package br.com.attornatus.pessoas.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviceConfiguration extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<DefaultResponse> handleAllExceptions(RuntimeException ex, WebRequest request) {
        log.error(ex.getLocalizedMessage(), ex);
        Optional<ResponseStatus> annotationResponse = getResponseAnnotation(ex.getClass());
        HttpStatus httpStatus = BAD_REQUEST;
        String status = separateInformation(annotationResponse , httpStatus);
        return new ResponseEntity<>(new DefaultResponse(status, extractMessage(ex)), httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<DefaultResponse> handleAllExceptions(Exception ex, WebRequest request) {
        log.error(ex.getLocalizedMessage(), ex);
        Optional<ResponseStatus> annotationResponse = getResponseAnnotation(ex.getClass());
        HttpStatus httpStatus = BAD_REQUEST;
        String status = separateInformation(annotationResponse , httpStatus);
        return new ResponseEntity<>(new DefaultResponse(status, extractMessage(ex)), httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors())
            errors.add(error.getField() + ": " + error.getDefaultMessage());

        for (ObjectError error : ex.getBindingResult().getGlobalErrors())
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());

        DefaultResponse response = new DefaultResponse(BAD_REQUEST.name(), errors);

        return handleExceptionInternal(ex, response, headers, BAD_REQUEST, request);
    }

    private String separateInformation (Optional<ResponseStatus> annotationResponse, HttpStatus httpStatus) {
        String status = httpStatus.name();
        try {
            if(annotationResponse.isPresent()){
                httpStatus = annotationResponse.get().value();

                if(Strings.isEmpty(annotationResponse.get().reason())) {
                    status = httpStatus.name();
                }else{
                    status = annotationResponse.get().reason();
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return status;
    }

    private String extractMessage(Exception ex) {
        return Strings.isEmpty(ex.getMessage()) ? ex.getClass().getSimpleName() : ex.getMessage();
    }

    private Optional<ResponseStatus> getResponseAnnotation(Class<?> exceptionClass) {
        return Optional.ofNullable(AnnotationUtils.findAnnotation(exceptionClass, ResponseStatus.class));
    }
}
