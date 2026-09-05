package com.google.main.service;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import module java.base;

public class Hello {

    static void main() {
        IO.println("Hello");

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6,
                1, 2, 3, 4, 5, 61, 2, 3, 4, 5, 61, 2, 3, 4, 5,
                61, 2, 3, 4, 5, 61, 2, 3, 4, 5, 61, 2, 3, 4, 5,
                61, 2, 3, 4, 5, 61, 2, 3, 4, 5, 61, 2, 3, 4, 5,
                61, 2, 3, 4, 5, 61, 2, 3, 4, 5, 61, 2, 3, 4, 5,
                61, 2, 3, 4, 5, 61, 2, 3, 4, 5, 61, 2, 3, 4, 5,
                61, 2, 3, 4, 5, 61, 2, 3, 4, 5, 61, 2, 3, 4, 5, 6);

        Optional<Integer> max = list.stream()
                .parallel()
                .map(n -> n * 2)
                .distinct()
                .skip(1)
//                .peek(n -> System.out.println(n))
                .max((a, b) -> a - b);

        if (max.isPresent()) {
            System.out.println(max.get());
        }

        Gson gson = new Gson();

        String hello = gson.toJson(Map.of(1, "Hello"));

        IO.println(hello);

        String text = "hello world";

        IO.println(StringUtils.capitalize(text));
        IO.println(StringUtils.reverse(text));

        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);

        flux.map(n -> n * 2)
                .filter(n -> n > 3)
                .subscribe(System.out::println);

        flux.map(n -> n * 2)
                .filter(n -> n > 3)
                .subscribe(
                        System.out::println,
                        error -> System.out.println(error.getMessage()),
                        () -> System.out.println("DONE")
                );


        Mono<Integer> mono = Mono.just(2);

        Disposable subscribe = mono.map(n -> n * 2 + 3)
                .subscribe(
                        System.out::println
                );

        IO.println(subscribe.isDisposed());
    }
}
