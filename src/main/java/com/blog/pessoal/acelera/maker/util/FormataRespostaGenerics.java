package com.blog.pessoal.acelera.maker.util;

import java.util.List;
import java.util.function.Function;

public class FormataRespostaGenerics {

    public static  <E, D> List<D> retornaFormatado(List<E> entidade, Function<E, D> conversor) {
        return entidade.stream()
                .map(conversor)
                .toList();
    }

}
