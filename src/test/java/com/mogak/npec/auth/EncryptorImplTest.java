package com.mogak.npec.auth;

import com.mogak.npec.auth.domain.Encryptor;
import com.mogak.npec.auth.domain.EncryptorImpl;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class EncryptorImplTest {
    @Test
    void encryptText() {
        Encryptor encryptor = new EncryptorImpl();
        String encryptText = encryptor.encrypt("1234");

        assertThat(encryptText).isNotEqualTo("1234");
    }
}
