package com.authcore.modulo.autenticacao.adaptador.seguranca;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

/**
 * Cifra / decifra texto UTF-8 com AES-256-GCM (IV aleatório de 12 B prefixado ao payload, tudo em Base64).
 */
@Component
public class ServicoCifradoCampoTextoUtf8 {

    private static final int TAM_IV = 12;
    private static final int TAM_TAG_BITS = 128;
    private static final String AES_GCM = "AES/GCM/NoPadding";

    private final SecretKeySpec chaveAes;

    public ServicoCifradoCampoTextoUtf8(PropriedadesRepousoCriptografia props) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(props.segredoMestreArmazenamento().getBytes(StandardCharsets.UTF_8));
            this.chaveAes = new SecretKeySpec(digest, "AES");
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível derivar chave AES de repouso.", e);
        }
    }

    public String cifrar(String textoPuro) {
        if (textoPuro == null) {
            return null;
        }
        try {
            byte[] iv = new byte[TAM_IV];
            new SecureRandom().nextBytes(iv);
            Cipher cipher = Cipher.getInstance(AES_GCM);
            cipher.init(Cipher.ENCRYPT_MODE, chaveAes, new GCMParameterSpec(TAM_TAG_BITS, iv));
            byte[] cifrado = cipher.doFinal(textoPuro.getBytes(StandardCharsets.UTF_8));
            ByteBuffer buf = ByteBuffer.allocate(iv.length + cifrado.length);
            buf.put(iv);
            buf.put(cifrado);
            return Base64.getEncoder().encodeToString(buf.array());
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao cifrar campo.", e);
        }
    }

    public String decifrar(String textoCifradoBase64) {
        if (textoCifradoBase64 == null || textoCifradoBase64.isBlank()) {
            return null;
        }
        try {
            byte[] tudo = Base64.getDecoder().decode(textoCifradoBase64);
            ByteBuffer buf = ByteBuffer.wrap(tudo);
            byte[] iv = new byte[TAM_IV];
            buf.get(iv);
            byte[] cifrado = new byte[buf.remaining()];
            buf.get(cifrado);
            Cipher cipher = Cipher.getInstance(AES_GCM);
            cipher.init(Cipher.DECRYPT_MODE, chaveAes, new GCMParameterSpec(TAM_TAG_BITS, iv));
            byte[] puro = cipher.doFinal(cifrado);
            return new String(puro, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao decifrar campo armazenado.", e);
        }
    }
}
