package com.authcore.comum.web;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.authcore.comum.excecao.CredenciaisInvalidasException;
import com.authcore.comum.excecao.NaoEncontradoException;
import com.authcore.comum.excecao.OperacaoNaoPermitidaException;
import com.authcore.comum.excecao.RegraNegocioException;

@RestControllerAdvice
public class ManipuladorExcecaoGlobalController {

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> tratarNaoEncontrado(NaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<Map<String, String>> tratarNaoAutorizado(CredenciaisInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, String>> tratarRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<Map<String, String>> tratarProibido(OperacaoNaoPermitidaException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(MethodArgumentNotValidException ex) {
        var erros =
                ex.getBindingResult().getFieldErrors().stream()
                        .collect(
                                Collectors.toMap(
                                        FieldError::getField,
                                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "inválido",
                                        (a, b) -> a,
                                        LinkedHashMap::new));
        var corpo = new LinkedHashMap<String, Object>();
        corpo.put("mensagem", "Dados inválidos no corpo da requisição.");
        corpo.put("detalhesCampos", erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> tratarJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        Map.of(
                                "mensagem",
                                "JSON inválido ou campos incompatíveis. Verifique nomes dos campos (ex.: emailCorporativo, senhaAcesso, nomeCompletoTitular)."));
    }
}
