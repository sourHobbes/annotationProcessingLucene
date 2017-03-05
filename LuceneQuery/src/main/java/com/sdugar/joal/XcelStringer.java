package com.sdugar.joal;

import java.util.List;

import org.jooq.lambda.Seq;

/**
 * Created by sourabhdugar on 3/5/17.
 */
public class XcelStringer {
    public static void main(String[] args) {
        int max = 3;

        List<String> alphabet = Seq
                .rangeClosed('A', 'Z')
                .map(Object::toString)
                .map(a -> a + ", ")
                .toList();

        Seq.rangeClosed(1, max)
                .flatMap(length ->
                        Seq.rangeClosed(1, length - 1)
                                .foldLeft(Seq.seq(alphabet), (s, i) ->
                                        s.crossJoin(Seq.seq(alphabet))
                                                .map(t -> t.v1.replace(", ", "") + t.v2)))
                .forEach(System.out::print);
    }
}
