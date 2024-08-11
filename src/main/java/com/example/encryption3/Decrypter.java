package com.example.encryption3;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

class Decrypter {
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = new byte[]{ 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

    public static String decrypt(String encryptedData, SecretKeySpec key) throws Exception {
        byte[] keyBytes = key.getEncoded(); // Get the key bytes from the SecretKeySpec
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

}