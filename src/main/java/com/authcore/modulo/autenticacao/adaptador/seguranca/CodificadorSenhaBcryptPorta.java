package com.authcore.modulo.autenticacao.adaptador.seguranca;

import com.authcore.modulo.autenticacao.dominio.PortaCodificadorSenha;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Primary
@Component
public class CodificadorSenhaBcryptPorta implements PortaCodificadorSenha {

    private final PasswordEncoder enc;

    public CodificadorSenhaBcryptPorta(PasswordEncoder enc) {
        this.enc = enc;
    }

    @Override
    public String codificar(String senhaPura) {
        return enc.encode(senhaPura);
    }

    @Override
    public boolean corresponde(String senhaPura, String hashArmazenado) {
        return enc.matches(senhaPura, hashArmazenado);
    }
}
