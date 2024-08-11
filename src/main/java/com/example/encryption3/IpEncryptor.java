package com.example.encryption3;// IpEncryptor.java

public class IpEncryptor {
    public static String encryptIp(String ip, String key) {
        byte[] ipBytes = ip.getBytes();
        byte[] keyBytes = key.getBytes();

        byte[] encryptedBytes = new byte[ipBytes.length];
        for (int i = 0; i < ipBytes.length; i++) {
            encryptedBytes[i] = (byte) (ipBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        return new String(encryptedBytes);
    }

    public static String decryptIp(String encryptedIp, String key) {
        byte[] encryptedBytes = encryptedIp.getBytes();
        byte[] keyBytes = key.getBytes();

        byte[] decryptedBytes = new byte[encryptedBytes.length];
        for (int i = 0; i < encryptedBytes.length; i++) {
            decryptedBytes[i] = (byte) (encryptedBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        return new String(decryptedBytes);
    }
}