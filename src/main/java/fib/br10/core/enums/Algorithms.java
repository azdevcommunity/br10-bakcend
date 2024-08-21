package fib.br10.core.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum Algorithms {
    // Symmetric Key Algorithms
    AES("AES"),
    DES("DES"),
    TRIPLE_DES("DESede"),
    BLOWFISH("Blowfish"),

    // Asymmetric Key Algorithms
    RSA("RSA"),
    DSA("DSA"),
    EC("EC"),

    // Hashing Algorithms
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA512("SHA-512"),

    // MAC Algorithms
    HMAC_SHA256("HmacSHA256"),
    HMAC_SHA512("HmacSHA512");

    private final String value;

    Algorithms(String value) {
        this.value = value;
    }

    public static Algorithms fromValue(String value) {
        for (Algorithms algorithm : Algorithms.values()) {
            if (Objects.equals(algorithm.value, value)) {
                return algorithm;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
