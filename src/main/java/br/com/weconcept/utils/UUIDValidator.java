package br.com.weconcept.utils;

import java.util.UUID;

public class UUIDValidator {
    public static boolean isValidUUID(String uuidStr) {
        try {
            UUID.fromString(uuidStr);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
