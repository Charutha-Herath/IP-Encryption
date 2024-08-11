package com.example.encryption3;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

class Encrypter {
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = new byte[]{ 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

    public static String encrypt(String data,SecretKey key) throws Exception {
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }


}