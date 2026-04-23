package com.authcore.modulo.autenticacao.config;

import java.util.UUID;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.authcore.modulo.autenticacao.adaptador.persistencia.ContaAcessoEntity;
import com.authcore.modulo.autenticacao.adaptador.persistencia.ContaAcessoJpaRepositorio;
import com.authcore.modulo.autenticacao.dominio.PapelAcessoSistema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Semente opcional p/ testar o fluxo sem cadastro prévio. Remova ou proteja p/ produção.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CarregadorContasDemonstracao implements ApplicationRunner {

    private static final String EMAIL_DEMO = "demonstracao@authcore.local";
    private static final String SENHA_DEMO = "demonstracao123";

    private static final String EMAIL_PROPRIETARIO = "gustavoavdcarmo@gmail.com";
    private static final String SENHA_PROPRIETARIO = "adminadmin";

    private final ContaAcessoJpaRepositorio repositorio;
    private final PasswordEncoder codificador;

    @Override
    public void run(ApplicationArguments args) {
        criarSeAindaNaoExiste(
                EMAIL_DEMO,
                SENHA_DEMO,
                "Conta Interna Demo",
                PapelAcessoSistema.USUARIO_CONVIDADO);
        criarSeAindaNaoExiste(
                EMAIL_PROPRIETARIO,
                SENHA_PROPRIETARIO,
                "Gustavo — proprietário plataforma",
                PapelAcessoSistema.PROPRIETARIO_PLATAFORMA);
        garantirPapelProprietarioNaContaGustavo();
    }

    /**
     * Se a tabela veio de versões antigas (sem coluna de papel) ou a conta existia sem
     * {@link PapelAcessoSistema#PROPRIETARIO_PLATAFORMA}, corrige p/ acesso completo.
     */
    private void garantirPapelProprietarioNaContaGustavo() {
        repositorio
                .findByEmailCorporativo(EMAIL_PROPRIETARIO.toLowerCase())
                .filter(e -> e.getNivelPapelAcesso() != PapelAcessoSistema.PROPRIETARIO_PLATAFORMA)
                .ifPresent(e -> {
                    e.setNivelPapelAcesso(PapelAcessoSistema.PROPRIETARIO_PLATAFORMA);
                    repositorio.save(e);
                    log.info("Papel da conta {} atualizado para {}.", EMAIL_PROPRIETARIO, PapelAcessoSistema.PROPRIETARIO_PLATAFORMA);
                });
    }

    private void criarSeAindaNaoExiste(
            String emailCorporativo, String senhaNaoCifrada, String nomeExibicao, PapelAcessoSistema nivel) {
        if (repositorio.findByEmailCorporativo(emailCorporativo.toLowerCase()).isEmpty()) {
            var e = new ContaAcessoEntity();
            e.setId(UUID.randomUUID());
            e.setEmailCorporativo(emailCorporativo.trim().toLowerCase());
            e.setHashSenha(codificador.encode(senhaNaoCifrada));
            e.setNomeApresentacao(nomeExibicao);
            e.setAtivo(true);
            e.setNivelPapelAcesso(nivel);
            repositorio.save(e);
            log.info("Conta semente criada: emailCorporativo={} nivelPapelAcesso={}", emailCorporativo, nivel);
        }
    }
}
