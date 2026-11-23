package br.com.reelbook.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Guarda senhas com hash (SHA-256 + salt) em vez de texto puro.
 *
 * O valor salvo em USUARIO.SENHA_HASH tem o formato "salt:hash"
 * (os dois em Base64) e cabe em VARCHAR(255).
 *
 * Onde usar:
 *  - CadastroServlet:  new Usuario(nome, email, SenhaUtil.gerarHash(senha))
 *  - UsuarioDAO.autenticar: buscar o usuário só pelo e-mail e depois
 *    conferir com SenhaUtil.confere(senhaDigitada, usuario.getSenhaHash())
 */
public class SenhaUtil {

    private static final SecureRandom ALEATORIO = new SecureRandom();

    private SenhaUtil() {
    }

    /** Gera "salt:hash" para gravar no banco. */
    public static String gerarHash(String senha) {
        byte[] salt = new byte[16];
        ALEATORIO.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt)
                + ":" + calcular(salt, senha);
    }

    /** Confere a senha digitada com o valor guardado no banco. */
    public static boolean confere(String senha, String armazenado) {
        if (senha == null || armazenado == null || !armazenado.contains(":")) {
            return false;
        }

        try {
            String[] partes = armazenado.split(":", 2);
            byte[] salt = Base64.getDecoder().decode(partes[0]);

            return MessageDigest.isEqual(
                    calcular(salt, senha).getBytes(StandardCharsets.UTF_8),
                    partes[1].getBytes(StandardCharsets.UTF_8));

        } catch (IllegalArgumentException e) {
            // valor antigo (texto puro) ou corrompido: não confere
            return false;
        }
    }

    private static String calcular(byte[] salt, String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);

            return Base64.getEncoder().encodeToString(
                    md.digest(senha.getBytes(StandardCharsets.UTF_8)));

        } catch (Exception e) {
            throw new IllegalStateException("Erro ao calcular o hash", e);
        }
    }
}
