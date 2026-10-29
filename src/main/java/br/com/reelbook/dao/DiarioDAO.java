package br.com.reelbook.dao;

import br.com.reelbook.util.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiarioDAO {

    public static class DiarioDTO {
        public int idFilme;
        public String titulo;
        public int ano;
        public String posterUrl;
        public Double nota;
        public String comentario;
        public LocalDate dataRegistro;
    }

    public List<DiarioDTO> listarPorUsuario(int idUsuario) throws SQLException {
        List<DiarioDTO> lista = new ArrayList<>();
        String sql = "SELECT f.ID_FILME, f.TITULO, f.ANO, f.POSTER_URL, "
                   + "       rp.NOTA, rp.DATA_REGISTRO, r.TEXTO AS COMENTARIO "
                   + "FROM REGISTRO_PESSOAL rp "
                   + "JOIN FILME f ON rp.ID_FILME = f.ID_FILME "
                   + "LEFT JOIN RESENHA r ON r.ID_USUARIO = rp.ID_USUARIO AND r.ID_FILME = rp.ID_FILME "
                   + "WHERE rp.ID_USUARIO = ? AND rp.STATUS = 'ASSISTIDO' "
                   + "ORDER BY rp.DATA_REGISTRO DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DiarioDTO dto = new DiarioDTO();
                    dto.idFilme = rs.getInt("ID_FILME");
                    dto.titulo = rs.getString("TITULO");
                    dto.ano = rs.getInt("ANO");
                    dto.posterUrl = rs.getString("POSTER_URL");
                    BigDecimal n = rs.getBigDecimal("NOTA");
                    dto.nota = (n != null) ? n.doubleValue() : null;
                    dto.comentario = rs.getString("COMENTARIO");
                    Date d = rs.getDate("DATA_REGISTRO");
                    dto.dataRegistro = (d != null) ? d.toLocalDate() : LocalDate.now();
                    lista.add(dto);
                }
            }
        }
        return lista;
    }

    public void salvarOuAtualizar(int idUsuario, int idFilme, Double nota, String comentario, LocalDate data) throws SQLException {
        String sqlRP = "MERGE INTO REGISTRO_PESSOAL (ID_USUARIO, ID_FILME, STATUS, FAVORITO, NOTA, DATA_REGISTRO) "
                     + "KEY (ID_USUARIO, ID_FILME) VALUES (?, ?, 'ASSISTIDO', FALSE, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(sqlRP)) {
                    ps.setInt(1, idUsuario);
                    ps.setInt(2, idFilme);
                    if (nota != null) ps.setDouble(3, nota); else ps.setNull(3, Types.DECIMAL);
                    ps.setDate(4, Date.valueOf(data != null ? data : LocalDate.now()));
                    ps.executeUpdate();
                }

                if (comentario != null && !comentario.trim().isEmpty()) {
                    try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM RESENHA WHERE ID_USUARIO = ? AND ID_FILME = ?")) {
                        psDel.setInt(1, idUsuario);
                        psDel.setInt(2, idFilme);
                        psDel.executeUpdate();
                    }

                    String insertResenha = "INSERT INTO RESENHA (ID_USUARIO, ID_FILME, TEXTO, DATA_PUBLICACAO) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement psIns = conn.prepareStatement(insertResenha)) {
                        psIns.setInt(1, idUsuario);
                        psIns.setInt(2, idFilme);
                        psIns.setString(3, comentario.trim());
                        psIns.setDate(4, Date.valueOf(data != null ? data : LocalDate.now()));
                        psIns.executeUpdate();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void excluir(int idUsuario, int idFilme) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM RESENHA WHERE ID_USUARIO = ? AND ID_FILME = ?")) {
                    ps.setInt(1, idUsuario);
                    ps.setInt(2, idFilme);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM REGISTRO_PESSOAL WHERE ID_USUARIO = ? AND ID_FILME = ?")) {
                    ps.setInt(1, idUsuario);
                    ps.setInt(2, idFilme);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}