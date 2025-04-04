package com.blog.pessoal.acelera.maker.controller;

import com.blog.pessoal.acelera.maker.DTO.tema.TemaDTO;
import com.blog.pessoal.acelera.maker.exception.TemaExisteException;
import com.blog.pessoal.acelera.maker.model.Resposta;
import com.blog.pessoal.acelera.maker.model.Tema;
import com.blog.pessoal.acelera.maker.service.TemaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/temas")
public class TemaController {

    @Autowired
    private TemaService temaService;

    @PostMapping
    public ResponseEntity<String> criarPostagem(@Valid @RequestBody TemaDTO temaDTO)
            throws TemaExisteException, RuntimeException {
        Resposta resposta = temaService.criarTema(temaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta.getMensagem());

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizaTema(@PathVariable Long id,@Valid @RequestBody TemaDTO temaDTO) throws TemaExisteException {
        Resposta resposta = temaService.atualiza(id ,temaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta.getMensagem());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeTema(@PathVariable Long id) {
        Resposta resposta = temaService.realizaDelete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(resposta.getMensagem());
    }

    @GetMapping
    public ResponseEntity<List<Tema>> listaTemas() {
        return ResponseEntity.ok(temaService.buscaTodosTemas());
    }

}
