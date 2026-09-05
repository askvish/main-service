package com.google.main.service;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.SequencedMap;
import java.util.function.BiFunction;

public class Java21Demo {

    static void main() {
        Thread thread = new Thread(
                () -> {
                    IO.println("Hello from Thread");
                    IO.println();
                }
        );

//        thread.start();

        Thread thread1 = Thread.startVirtualThread(
                () -> {
                    for (int i = 0; i < 100; i++) {
                        IO.print(i);
                        if (i != 99) {
                            IO.print(", ");
                        }
                    }
                    IO.println();
                }
        );

        try {
            thread1.join();
        } catch (InterruptedException _) {
            // Ignore
        }

        SequencedMap<Integer, String> map = new LinkedHashMap<>();

        map.put(1, "Ashok");
        map.put(3, "Aditi");
        map.put(4, "Shiv");
        map.put(2, "Parvati");

        IO.println(map.firstEntry());
        IO.println(map.reversed());

        Object value;
        Object obj;
        Random random = new Random();
        if (random.nextInt() > 1) {
            obj = new Person(1, "Ashok", new Address("Hata", 274203));
            value = 16;
        } else {
            obj = null;
            value = 25;
        }

        // Old way
//        if (obj instanceof Person p) {
//
//            int id = p.id();
//            String name = p.name();
//
//            IO.println(id + ": " + name);
//        }

        if (obj instanceof Person(int id, String name, Address(String city, int pinCode))) {
            IO.println(id + ": " + name + ", from " + city + ": " + pinCode);
        }

        if (obj instanceof String _) {
            IO.println("Unnamed Pattern");
        }

        switch (value) {
            case null -> throw new IllegalArgumentException("value is null");
            case Integer i when i < 18 -> IO.println("Minor");
            case Integer _ -> IO.println("Adult");
            default -> throw new IllegalArgumentException("Invalid data type for age");
        }


        BiFunction<String, String, String> concat =
                (var a, @Nonnull var b) -> a + b;

        IO.println(
                concat.apply("Hello ", "Java")
        );

    }
}

record Person(
        int id,
        String name,
        Address address
) {
}

record Address(String city, int pinCode) {
}