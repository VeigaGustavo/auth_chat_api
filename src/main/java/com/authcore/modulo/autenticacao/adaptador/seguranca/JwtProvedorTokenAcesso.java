package com.authcore.modulo.autenticacao.adaptador.seguranca;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import com.authcore.modulo.autenticacao.dominio.ContaAcesso;
import com.authcore.modulo.autenticacao.dominio.PapelAcessoSistema;
import com.authcore.modulo.autenticacao.dominio.PortaProvedorTokenAcesso;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvedorTokenAcesso implements PortaProvedorTokenAcesso {

    private static final String CLAIM_NOME_EXIBICAO = "nomeExibicao";
    private static final String CLAIM_NIVEL_PAPEL_ACESSO = "nivelPapelAcesso";
    private static final String CLAIM_PERMISSOES_ACESSO = "permissoesAcessoEfetivas";

    private final PropriedadesAcessoTokenJwt props;
    private final SecretKey chaveHmac;

    public JwtProvedorTokenAcesso(PropriedadesAcessoTokenJwt props) {
        this.props = props;
        this.chaveHmac = Keys.hmacShaKeyFor(props.chaveAssinatura().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String emitirToken(ContaAcesso contaAcesso) {
        var nivel = contaAcesso.getNivelPapelAcesso() != null
                ? contaAcesso.getNivelPapelAcesso()
                : PapelAcessoSistema.USUARIO_CONVIDADO;
        return Jwts.builder()
                .setSubject(contaAcesso.getId().toString())
                .claim(CLAIM_NOME_EXIBICAO, contaAcesso.nomeParaExibicaoPublica())
                .claim(CLAIM_NIVEL_PAPEL_ACESSO, nivel.name())
                .claim(CLAIM_PERMISSOES_ACESSO, nivel.listarNomesChavePermissoes())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + props.duracaoValidadeMilis()))
                .signWith(chaveHmac, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean tokenEstaValido(String token) {
        return resumoSeValido(limparBearer(token)).isPresent();
    }

    @Override
    public long validadeAproximadaEmSegundos() {
        return Math.max(1, props.duracaoValidadeMilis() / 1000L);
    }

    @Override
    public Optional<ResumoAcessoContaToken> resumoSeValido(String token) {
        String puro = limparBearer(token);
        if (puro == null || puro.isBlank()) {
            return Optional.empty();
        }
        try {
            Claims c = Jwts.parserBuilder()
                    .setSigningKey(chaveHmac)
                    .build()
                    .parseClaimsJws(puro)
                    .getBody();
            var id = UUID.fromString(c.getSubject());
            var nome = c.get(CLAIM_NOME_EXIBICAO, String.class);
            return Optional.of(new ResumoAcessoContaToken(id, nome != null ? nome : "—"));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static String limparBearer(String t) {
        if (t == null) {
            return null;
        }
        if (t.startsWith("Bearer ")) {
            return t.substring(7);
        }
        return t;
    }
}
