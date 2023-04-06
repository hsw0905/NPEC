package com.mogak.npec.auth;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EncryptorImplTest {
    @Test
    void encryptText() {
        Encryptor encryptor = new EncryptorImpl();
        String encryptText = encryptor.encrypt("1234");

        assertThat(encryptText).isNotEqualTo("1234");
    }
}
