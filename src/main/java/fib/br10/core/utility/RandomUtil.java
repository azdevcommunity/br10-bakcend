package fib.br10.core.utility;


import java.util.Random;
import java.util.UUID;


public class RandomUtil {
    private static final Random RANDOM = new Random();
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC_CHARACTERS = "0123456789";

    public static int randomInt(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public static double randomDouble(double min, double max) {
        return min + (max - min) * RANDOM.nextDouble();
    }

    public static String randomString(int length, StringType stringType) {
        String characters = getCharacters(stringType);
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();
    }

    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }

    public static long randomLong(long min, long max) {
        return min + (long) ((max - min) * RANDOM.nextDouble());
    }

    public static UUID getUUID() {
        return UUID.randomUUID();
    }

    public static String getUUIDAsString() {
        return UUID.randomUUID().toString();
    }

    public static String getUUIDAsStringWithoutDash() {
        return getUUIDAsStringWithoutDash(UUID.randomUUID());
    }

    public static String getUUIDAsStringWithoutDash(String uuid) {
        return getUUIDAsStringWithoutDash(UUID.fromString(uuid));
    }

    public static String getUUIDAsStringWithoutDash(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    private static String getCharacters(StringType stringType) {
        return switch (stringType) {
            case UPPERCASE -> UPPERCASE_CHARACTERS;
            case LOWERCASE -> LOWERCASE_CHARACTERS;
            case NUMERIC -> NUMERIC_CHARACTERS;
            case MIXED -> UPPERCASE_CHARACTERS + LOWERCASE_CHARACTERS + NUMERIC_CHARACTERS;
        };
    }

    public enum StringType {
        UPPERCASE,
        LOWERCASE,
        NUMERIC,
        MIXED
    }
}

