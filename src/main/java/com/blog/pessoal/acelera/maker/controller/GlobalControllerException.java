package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.exception.IntegridadeVioladaException;
import com.blog.pessoal.acelera.maker.exception.TemaExisteException;
import com.blog.pessoal.acelera.maker.exception.UsuarioJaExisteException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;


@RestControllerAdvice
public class GlobalControllerException {

    // Exceções relacionadas ao Tema
    @ExceptionHandler(TemaExisteException.class)
    public ResponseEntity<String> trataDuplicidade(TemaExisteException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }

    // Exceções relacionadas ao Usuário
    @ExceptionHandler(UsuarioJaExisteException.class)
    public ResponseEntity<String> trataDuplicidadeUsuario(UsuarioJaExisteException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }

    @ExceptionHandler(IntegridadeVioladaException.class)
    public ResponseEntity<String> handleRollbackException(IntegridadeVioladaException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<String> handleRollbackException(TransactionSystemException ex) {
        return ResponseEntity.badRequest().body("Erro ao salvar dados.");
    }

    // Exceções genéricas USUÁRIO/TEMA/POSTAGEM
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> trataErroBusca(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> trataExcecaoGenerica(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor");
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> trataExcecaoGenerica(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> trataViolacaoArgumento(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}


