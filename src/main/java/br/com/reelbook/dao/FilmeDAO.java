package br.com.reelbook.dao;

import br.com.reelbook.model.Filme;
import br.com.reelbook.model.Genero;
import br.com.reelbook.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FilmeDAO {

    public List<Filme> listar(
            String busca,
            Integer ano,
            Integer idGenero,
            boolean incluirInativos
    ) throws SQLException {

        List<Filme> filmes = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT f.ID_FILME, f.TITULO, f.SINOPSE, " +
                        "f.ANO, f.DURACAO, f.POSTER_URL, f.ATIVO " +
                        "FROM FILME f " +
                        "LEFT JOIN FILME_GENERO fg " +
                        "ON fg.ID_FILME = f.ID_FILME WHERE 1=1 "
        );

        if (!incluirInativos) {
            sql.append(" AND f.ATIVO = TRUE ");
        }

        if (busca != null && !busca.isBlank()) {
            sql.append(" AND LOWER(f.TITULO) LIKE ? ");
        }

        if (ano != null) {
            sql.append(" AND f.ANO = ? ");
        }

        if (idGenero != null) {
            sql.append(" AND fg.ID_GENERO = ? ");
        }

        sql.append(" ORDER BY f.TITULO");

        try (
                Connection c = ConnectionFactory.getConnection();
                PreparedStatement ps = c.prepareStatement(sql.toString())
        ) {
            int i = 1;

            if (busca != null && !busca.isBlank()) {
                ps.setString(
                        i++,
                        "%" + busca.trim().toLowerCase() + "%"
                );
            }

            if (ano != null) {
                ps.setInt(i++, ano);
            }

            if (idGenero != null) {
                ps.setInt(i, idGenero);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filmes.add(mapear(rs));
                }
            }
        }

        for (Filme filme : filmes) {
            filme.setGeneros(
                    listarGenerosDoFilme(filme.getIdFilme())
            );
        }

        return filmes;
    }

    public Filme buscarPorId(int id) throws SQLException {

        String sql =
                "SELECT ID_FILME, TITULO, SINOPSE, ANO, DURACAO, " +
                        "POSTER_URL, ATIVO FROM FILME WHERE ID_FILME = ?";

        try (
                Connection c = ConnectionFactory.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Filme filme = mapear(rs);

                filme.setGeneros(
                        listarGenerosDoFilme(id)
                );

                return filme;
            }
        }
    }

    public void salvar(
            Filme filme,
            List<Integer> idsGeneros
    ) throws SQLException {

        String sql =
                "INSERT INTO FILME " +
                        "(TITULO, SINOPSE, ANO, DURACAO, POSTER_URL, ATIVO) " +
                        "VALUES (?, ?, ?, ?, ?, TRUE)";

        try (Connection c = ConnectionFactory.getConnection()) {

            c.setAutoCommit(false);

            try {
                try (
                        PreparedStatement ps = c.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
                ) {
                    preencher(ps, filme);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException(
                                    "Não foi possível obter o ID do filme."
                            );
                        }

                        filme.setIdFilme(keys.getInt(1));
                    }
                }

                salvarGeneros(
                        c,
                        filme.getIdFilme(),
                        idsGeneros
                );

                c.commit();

            } catch (SQLException e) {
                c.rollback();
                throw e;

            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void atualizar(
            Filme filme,
            List<Integer> idsGeneros
    ) throws SQLException {

        String sql =
                "UPDATE FILME SET TITULO = ?, SINOPSE = ?, " +
                        "ANO = ?, DURACAO = ?, POSTER_URL = ? " +
                        "WHERE ID_FILME = ?";

        try (Connection c = ConnectionFactory.getConnection()) {

            c.setAutoCommit(false);

            try {
                try (PreparedStatement ps = c.prepareStatement(sql)) {

                    preencher(ps, filme);
                    ps.setInt(6, filme.getIdFilme());

                    if (ps.executeUpdate() == 0) {
                        throw new SQLException(
                                "Filme não encontrado."
                        );
                    }
                }

                try (
                        PreparedStatement ps = c.prepareStatement(
                                "DELETE FROM FILME_GENERO WHERE ID_FILME = ?"
                        )
                ) {
                    ps.setInt(1, filme.getIdFilme());
                    ps.executeUpdate();
                }

                salvarGeneros(
                        c,
                        filme.getIdFilme(),
                        idsGeneros
                );

                c.commit();

            } catch (SQLException e) {
                c.rollback();
                throw e;

            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void desativar(int id) throws SQLException {

        String sql =
                "UPDATE FILME SET ATIVO = FALSE WHERE ID_FILME = ?";

        try (
                Connection c = ConnectionFactory.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Genero> listarGenerosAtivos()
            throws SQLException {

        List<Genero> generos = new ArrayList<>();

        String sql =
                "SELECT ID_GENERO, NOME FROM GENERO " +
                        "WHERE ATIVO = TRUE " +
                        "ORDER BY ORDEM NULLS LAST, NOME";

        try (
                Connection c = ConnectionFactory.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                generos.add(
                        new Genero(
                                rs.getInt("ID_GENERO"),
                                rs.getString("NOME")
                        )
                );
            }
        }

        return generos;
    }

    private List<Genero> listarGenerosDoFilme(int idFilme)
            throws SQLException {

        List<Genero> generos = new ArrayList<>();

        String sql =
                "SELECT g.ID_GENERO, g.NOME " +
                        "FROM GENERO g " +
                        "JOIN FILME_GENERO fg " +
                        "ON fg.ID_GENERO = g.ID_GENERO " +
                        "WHERE fg.ID_FILME = ? " +
                        "ORDER BY g.NOME";

        try (
                Connection c = ConnectionFactory.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setInt(1, idFilme);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    generos.add(
                            new Genero(
                                    rs.getInt("ID_GENERO"),
                                    rs.getString("NOME")
                            )
                    );
                }
            }
        }

        return generos;
    }

    private void salvarGeneros(
            Connection c,
            int idFilme,
            List<Integer> ids
    ) throws SQLException {

        if (ids == null) {
            return;
        }

        String sql =
                "INSERT INTO FILME_GENERO (ID_FILME, ID_GENERO) " +
                        "SELECT ?, ID_GENERO FROM GENERO " +
                        "WHERE ID_GENERO = ? AND ATIVO = TRUE";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            for (Integer idGenero : ids) {
                if (idGenero == null) {
                    continue;
                }

                ps.setInt(1, idFilme);
                ps.setInt(2, idGenero);
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private Filme mapear(ResultSet rs) throws SQLException {

        Filme filme = new Filme();

        filme.setIdFilme(rs.getInt("ID_FILME"));
        filme.setTitulo(rs.getString("TITULO"));
        filme.setSinopse(rs.getString("SINOPSE"));
        filme.setAno(rs.getInt("ANO"));
        filme.setDuracao(rs.getInt("DURACAO"));
        filme.setPosterUrl(rs.getString("POSTER_URL"));
        filme.setAtivo(rs.getBoolean("ATIVO"));

        return filme;
    }

    private void preencher(
            PreparedStatement ps,
            Filme filme
    ) throws SQLException {

        ps.setString(1, filme.getTitulo().trim());
        ps.setString(2, filme.getSinopse());
        ps.setInt(3, filme.getAno());
        ps.setInt(4, filme.getDuracao());
        ps.setString(5, filme.getPosterUrl());
    }
}