package com.authcore.modulo.autenticacao.adaptador.seguranca;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

/**
 * Índices opacos p/ buscas (e-mail, token) sem persistir o valor em claro.
 */
@Component
public class ServicoIndiceDeterministicoRepouso {

    private static final String HMAC_ALGO = "HmacSHA256";

    private final byte[] chaveHmac;

    public ServicoIndiceDeterministicoRepouso(PropriedadesRepousoCriptografia props) {
        this.chaveHmac = props.segredoMestreArmazenamento().getBytes(StandardCharsets.UTF_8);
    }

    public String indiceParaEmailNormalizado(String emailCorporativoNormalizado) {
        return hmacHex("email:" + emailCorporativoNormalizado);
    }

    public String indiceParaTokenRedefinicaoSenha(String tokenOpaco) {
        return hmacHex("redef-senha:" + tokenOpaco);
    }

    private String hmacHex(String material) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            mac.init(new SecretKeySpec(chaveHmac, HMAC_ALGO));
            byte[] raw = mac.doFinal(material.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(raw);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao calcular índice de repouso.", e);
        }
    }
}
