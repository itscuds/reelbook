package br.com.reelbook.util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
        * Garante que exista um usuário com perfil CURADOR no banco H2.
        *
        * para administrar o catálogo (cadastrar, editar e desativar
        * filmes) e os gêneros.
        *
        * Login de teste:  curador@reelbook.com  /  curador123
        *
        * Para transformar outra conta em curador, no console do H2:
        *   UPDATE USUARIO SET PERFIL = 'CURADOR' WHERE EMAIL = 'email@exemplo.com';
        */
public class CuradorInicializador {
    public static final String EMAIL = "curador@reelbook.com";
    private static final String SENHA = "curador123";

    private CuradorInicializador() {
    }

    public static void inicializar() throws SQLException {
        try (Connection c = ConnectionFactory.getConnection()) {

            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT 1 FROM USUARIO WHERE EMAIL = ?")) {
                ps.setString(1, EMAIL);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // já existe: garante que a senha bate com o hash atual
                        try (PreparedStatement up = c.prepareStatement(
                                "UPDATE USUARIO SET SENHA_HASH = ?, PERFIL = 'CURADOR', "
                                        + "ATIVO = TRUE WHERE EMAIL = ?")) {
                            up.setString(1, SenhaUtil.gerarHash(SENHA));
                            up.setString(2, EMAIL);
                            up.executeUpdate();
                        }
                        return;
                    }
                }
            }

            try (PreparedStatement ins = c.prepareStatement(
                    "INSERT INTO USUARIO "
                            + "(NOME, EMAIL, SENHA_HASH, PERFIL, DATA_CADASTRO, ATIVO) "
                            + "VALUES (?, ?, ?, 'CURADOR', CURRENT_DATE, TRUE)")) {
                ins.setString(1, "Curador");
                ins.setString(2, EMAIL);
                ins.setString(3, SenhaUtil.gerarHash(SENHA));
                ins.executeUpdate();
            }
        }
    }
}

