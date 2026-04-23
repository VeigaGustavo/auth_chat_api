package com.authcore.modulo.autenticacao.config;

import java.util.UUID;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.authcore.modulo.autenticacao.dominio.ContaAcesso;
import com.authcore.modulo.autenticacao.dominio.ContaAcessoRepositorio;
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

    private static final String EMAIL_PROPRIETARIO = "proprietario@authcore.com";
    private static final String SENHA_PROPRIETARIO = "adminadmin";

    private final ContaAcessoRepositorio contaAcessoRepositorio;
    private final PasswordEncoder codificador;

    @Override
    public void run(ApplicationArguments args) {
        criarSeAindaNaoExiste(
                EMAIL_DEMO,
                SENHA_DEMO,
                "Conta Demonstração Interna",
                PapelAcessoSistema.USUARIO_CONVIDADO);
        criarSeAindaNaoExiste(
                EMAIL_PROPRIETARIO,
                SENHA_PROPRIETARIO,
                "Proprietário Plataforma Authcore",
                PapelAcessoSistema.PROPRIETARIO_PLATAFORMA);
        garantirPapelProprietarioNaContaPrincipal();
    }

    private void garantirPapelProprietarioNaContaPrincipal() {
        contaAcessoRepositorio
                .buscarPorEmailCorporativo(EMAIL_PROPRIETARIO.toLowerCase())
                .filter(c -> c.getNivelPapelAcesso() != PapelAcessoSistema.PROPRIETARIO_PLATAFORMA)
                .ifPresent(c -> {
                    var atualizada = ContaAcesso.builder()
                            .id(c.getId())
                            .emailCorporativo(c.getEmailCorporativo())
                            .hashSenha(c.getHashSenha())
                            .nomeCompletoTitular(c.getNomeCompletoTitular())
                            .ativo(c.isAtivo())
                            .nivelPapelAcesso(PapelAcessoSistema.PROPRIETARIO_PLATAFORMA)
                            .build();
                    contaAcessoRepositorio.salvar(atualizada);
                    log.info(
                            "Papel da conta {} atualizado para {}.",
                            EMAIL_PROPRIETARIO,
                            PapelAcessoSistema.PROPRIETARIO_PLATAFORMA);
                });
    }

    private void criarSeAindaNaoExiste(
            String emailCorporativo,
            String senhaNaoCifrada,
            String nomeCompletoTitular,
            PapelAcessoSistema nivel) {
        String email = emailCorporativo.trim().toLowerCase();
        if (contaAcessoRepositorio.buscarPorEmailCorporativo(email).isEmpty()) {
            var c = ContaAcesso.builder()
                    .id(UUID.randomUUID())
                    .emailCorporativo(email)
                    .hashSenha(codificador.encode(senhaNaoCifrada))
                    .nomeCompletoTitular(nomeCompletoTitular)
                    .ativo(true)
                    .nivelPapelAcesso(nivel)
                    .build();
            contaAcessoRepositorio.salvar(c);
            log.info("Conta semente criada: emailCorporativo={} nivelPapelAcesso={}", emailCorporativo, nivel);
        }
    }
}
