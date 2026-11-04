package com.ecommerce.order_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request){

        log.warn("Recurso no encontrado - Path: {}, Message: {}",
                request.getDescription(false), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("Recurso no encontrado");
        problemDetail.setType(URI.create("https://api.ecommerce.com/errors/not-found"));
        problemDetail.setProperty("Timestamp", Instant.now());

        problemDetail.setProperty("Resource", ex.getResourceName());
        problemDetail.setProperty("Field", ex.getFieldName());
        problemDetail.setProperty("Value", ex.getFieldValue());

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "La validación falló en uno o mas campos"
                );

        problemDetail.setTitle("Error de validación");
        problemDetail.setType(URI.create("https://api.ecommerce.com/errors/error-validation"));
        problemDetail.setProperty("Timestamp", Instant.now());

        Map<String, String> errorMap = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> {
                    errorMap.put(error.getField(), error.getDefaultMessage());
                }
        );

        problemDetail.setProperty("errors", errorMap);

        return problemDetail;
    }

    /**
     * Devuelve un error de entrada para JSON o parámetros de ruta inválidos.
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ProblemDetail handleInvalidRequest(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición o sus parámetros no son válidos");
        problemDetail.setTitle("Petición inválida");
        problemDetail.setProperty("Timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Informa del fallo de inventario sin exponer su respuesta interna.
     */
    @ExceptionHandler(InventoryServiceException.class)
    public ProblemDetail handleInventoryServiceException(InventoryServiceException ex) {
        log.error("Error al comunicarse con inventario", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY,
                "No se pudo completar la operación con inventario");
        problemDetail.setTitle("Error del servicio de inventario");
        problemDetail.setProperty("Timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex, WebRequest request){


        log.error("A ocurrido un error inesperado {}:  {}",
                request.getDescription(false), ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "A ocurrido un error inesperado. Por favor, contactar con el administrador"
        );

        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://api.ecommerce.com/errors/internal"));
        problemDetail.setProperty("Timestamp", Instant.now());

        return problemDetail;

    }














}
