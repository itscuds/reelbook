package br.com.reelbook.dao;

import br.com.reelbook.model.Usuario;
import br.com.reelbook.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class UsuarioDAO {

    public static class PerfilStatsDTO {
        public int totalListas;
        public int totalAssistidos;
        public int totalResenhas;
        public int totalFavoritos;
    }

    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM USUARIO WHERE EMAIL = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void cadastrar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO USUARIO (NOME, EMAIL, SENHA_HASH, PERFIL, DATA_CADASTRO, ATIVO) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenhaHash());
            ps.setString(4, usuario.getPerfil() != null ? usuario.getPerfil() : "USUARIO");
            ps.setDate(5, Date.valueOf(usuario.getDataCadastro() != null ? usuario.getDataCadastro() : LocalDate.now()));
            ps.setBoolean(6, usuario.isAtivo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        }
    }

    public Usuario autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT ID_USUARIO, NOME, EMAIL, SENHA_HASH, PERFIL, DATA_CADASTRO, ATIVO "
                + "FROM USUARIO WHERE EMAIL = ? AND SENHA_HASH = ? AND ATIVO = TRUE";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return converter(rs);
                }
            }
        }
        return null;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT ID_USUARIO, NOME, EMAIL, SENHA_HASH, PERFIL, DATA_CADASTRO, ATIVO "
                + "FROM USUARIO WHERE ID_USUARIO = ? AND ATIVO = TRUE";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return converter(rs);
                }
            }
        }
        return null;
    }

    public PerfilStatsDTO obterEstatisticas(int idUsuario) throws SQLException {
        PerfilStatsDTO stats = new PerfilStatsDTO();
        String sql = "SELECT "
                + "  (SELECT COUNT(*) FROM REGISTRO_PESSOAL WHERE ID_USUARIO = ? AND STATUS = 'ASSISTIDO') AS ASSISTIDOS, "
                + "  (SELECT COUNT(*) FROM RESENHA WHERE ID_USUARIO = ?) AS RESENHAS, "
                + "  (SELECT COUNT(*) FROM REGISTRO_PESSOAL WHERE ID_USUARIO = ? AND FAVORITO = TRUE) AS FAVORITOS";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            ps.setInt(3, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.totalListas = 0;
                    stats.totalAssistidos = rs.getInt("ASSISTIDOS");
                    stats.totalResenhas = rs.getInt("RESENHAS");
                    stats.totalFavoritos = rs.getInt("FAVORITOS");
                }
            }
        }
        return stats;
    }

    private Usuario converter(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("ID_USUARIO"));
        u.setNome(rs.getString("NOME"));
        u.setEmail(rs.getString("EMAIL"));
        u.setSenhaHash(rs.getString("SENHA_HASH"));
        u.setPerfil(rs.getString("PERFIL"));
        Date dt = rs.getDate("DATA_CADASTRO");
        u.setDataCadastro(dt != null ? dt.toLocalDate() : LocalDate.now());
        u.setAtivo(rs.getBoolean("ATIVO"));
        return u;
    }
}