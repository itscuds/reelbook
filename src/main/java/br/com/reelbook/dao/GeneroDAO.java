package br.com.reelbook.dao;

import br.com.reelbook.model.Genero;
import br.com.reelbook.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** CRUD da tabela GENERO (cadastrar, listar, renomear, desativar/reativar). */
public class GeneroDAO {

    private static final String COLUNAS =
            "ID_GENERO, NOME, DESCRICAO, ORDEM, ATIVO";

    /** Lista todos (ativos e inativos), para a tela de administração. */
    public List<Genero> listarTodos() throws SQLException {
        List<Genero> lista = new ArrayList<>();
        String sql = "SELECT " + COLUNAS + " FROM GENERO "
                + "ORDER BY ATIVO DESC, ORDEM NULLS LAST, NOME";

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(converter(rs));
            }
        }
        return lista;
    }

    public Genero buscarPorId(int id) throws SQLException {
        String sql = "SELECT " + COLUNAS + " FROM GENERO WHERE ID_GENERO = ?";

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? converter(rs) : null;
            }
        }
    }

    /** Nome é UNIQUE no banco; ignora maiúsculas e o próprio registro (ao renomear). */
    public boolean existeNome(String nome, int idIgnorado) throws SQLException {
        String sql = "SELECT 1 FROM GENERO "
                + "WHERE LOWER(NOME) = LOWER(?) AND ID_GENERO <> ?";

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setInt(2, idIgnorado);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void cadastrar(Genero g) throws SQLException {
        String sql = "INSERT INTO GENERO (NOME, DESCRICAO, ORDEM, ATIVO) "
                + "VALUES (?, ?, ?, TRUE)";

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps =
                     c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, g.getNome());
            ps.setString(2, g.getDescricao());
            if (g.getOrdem() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, g.getOrdem());
            }
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    g.setIdGenero(rs.getInt(1));
                }
            }
        }
    }

    /** Renomeia e atualiza descrição/ordem. */
    public boolean atualizar(Genero g) throws SQLException {
        String sql = "UPDATE GENERO SET NOME = ?, DESCRICAO = ?, ORDEM = ? "
                + "WHERE ID_GENERO = ?";

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, g.getNome());
            ps.setString(2, g.getDescricao());
            if (g.getOrdem() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, g.getOrdem());
            }
            ps.setInt(4, g.getIdGenero());
            return ps.executeUpdate() > 0;
        }
    }

    /** Desativar não apaga: os filmes continuam ligados ao gênero. */
    public boolean alterarAtivo(int id, boolean ativo) throws SQLException {
        String sql = "UPDATE GENERO SET ATIVO = ? WHERE ID_GENERO = ?";

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, ativo);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Genero converter(ResultSet rs) throws SQLException {
        Genero g = new Genero();
        g.setIdGenero(rs.getInt("ID_GENERO"));
        g.setNome(rs.getString("NOME"));
        g.setDescricao(rs.getString("DESCRICAO"));
        int ordem = rs.getInt("ORDEM");
        g.setOrdem(rs.wasNull() ? null : ordem);
        g.setAtivo(rs.getBoolean("ATIVO"));
        return g;
    }
}

