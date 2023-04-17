package com.mogak.npec.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptorImpl implements Encryptor {
    @Override
    public String encrypt(String text) {
        try {
            // MessageDigest 매번 인스턴스를 만든다?
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            return toHex(md.digest());

        } catch (NoSuchAlgorithmException e) {
            // Configuration
            throw new InvalidAlgorithmException("유효하지 않은 알고리즘입니다.");
        }
    }

    private String toHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
