package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.exception.PermissaoNaoAutorizada;
import com.blog.pessoal.acelera.maker.exception.TemaExisteException;
import com.blog.pessoal.acelera.maker.exception.UsuarioJaExisteException;

import com.blog.pessoal.acelera.maker.exception.UsuarioSenhaInvalidoException;
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

    @ExceptionHandler(UsuarioSenhaInvalidoException.class)
    public ResponseEntity<String> handleRollbackException(UsuarioSenhaInvalidoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PermissaoNaoAutorizada.class)
    public ResponseEntity<String> handleRollbackException(PermissaoNaoAutorizada e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> trataViolacaoArgumento(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

}


