package com.google.main.service;

public class Java17Demo {

    static void main() {
        Object obj = "Hello World";

        if (obj instanceof String s && s.length() > 5) {
            System.out.println("Long string: " + s);
        }
    }
}

sealed class Payment permits CardPayment, UPIPayment {

}

non-sealed class CardPayment extends Payment {

}

final class UPIPayment extends Payment {

}

//class CryptoPayment extends Payment {
//}

class DebitCardPayment extends CardPayment {

}