package com.mogak.npec.auth;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class EncryptorImpl implements Encryptor {

    @Override
    public String encrypt(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            return toHex(md.digest());

        } catch (NoSuchAlgorithmException e) {
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
