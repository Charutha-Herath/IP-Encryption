package com.example.encryption3;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGen {
    private static final String ALGORITHM = "AES";

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128); // 128-bit key size
        SecretKey key = keyGen.generateKey();
        return key;
    }

    public static String getKeyBytes(SecretKey key) {
        byte[] keyBytes = key.getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        return  base64Key;
    }
}