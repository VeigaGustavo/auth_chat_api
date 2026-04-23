package com.authcore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        properties = {
            "spring.datasource.url=jdbc:h2:mem:authcoretest;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false",
            "spring.datasource.username=sa",
            "spring.datasource.password=",
            "spring.jpa.hibernate.ddl-auto=create-drop",
            "spring.flyway.enabled=false",
            "acesso.cors.origens=*",
            "acesso.jwt.chaveAssinatura=0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef",
            "acesso.jwt.duracaoValidadeMilis=900000",
            "acesso.repouso.segredoMestreArmazenamento=0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef",
        })
class AutenticacaoChatAplicacaoTests {

    @Test
    void contextoCarrega() {
        // aplicação sobe; integração real com auth/ws fica p/ outro teste
    }
}
