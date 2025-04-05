package com.blog.pessoal.acelera.maker.util;

import java.util.List;
import java.util.function.Function;

public class FormataRespostaGenerics {

    // Formata Collection
    public static  <E, D> List<D> retornaListaFormatada(List<E> entidade, Function<E, D> conversor) {
        return entidade.stream()
                .map(conversor)
                .toList();
    }

    // Formata Objeto
    public static <E, D> D retornaFormatado(E entidade, Function<E, D> conversor) {
        return conversor.apply(entidade);
    }

}
