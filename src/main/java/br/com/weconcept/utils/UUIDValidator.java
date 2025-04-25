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

    public static boolean isValidUUID(String uuidStr, String uuidStr2) {
        try {
            UUID.fromString(uuidStr);
            UUID.fromString(uuidStr2);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
