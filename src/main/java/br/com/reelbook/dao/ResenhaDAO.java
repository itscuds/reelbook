package br.com.reelbook.dao;

import br.com.reelbook.util.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResenhaDAO {

    public static class ResenhaDTO {
        public int idResenha;
        public int idFilme;
        public String tituloFilme;
        public int anoFilme;
        public String posterUrl;
        public String texto;
        public Double nota;
        public LocalDate dataPublicacao;
    }

    public List<ResenhaDTO> listarPorUsuario(int idUsuario) throws SQLException {
        List<ResenhaDTO> lista = new ArrayList<>();
        String sql = "SELECT r.ID_RESENHA, r.TEXTO, r.DATA_PUBLICACAO, "
                   + "       f.ID_FILME, f.TITULO, f.ANO, f.POSTER_URL, rp.NOTA "
                   + "FROM RESENHA r "
                   + "JOIN FILME f ON r.ID_FILME = f.ID_FILME "
                   + "LEFT JOIN REGISTRO_PESSOAL rp ON rp.ID_FILME = r.ID_FILME AND rp.ID_USUARIO = r.ID_USUARIO "
                   + "WHERE r.ID_USUARIO = ? ORDER BY r.DATA_PUBLICACAO DESC, r.ID_RESENHA DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ResenhaDTO dto = new ResenhaDTO();
                    dto.idResenha = rs.getInt("ID_RESENHA");
                    dto.idFilme = rs.getInt("ID_FILME");
                    dto.tituloFilme = rs.getString("TITULO");
                    dto.anoFilme = rs.getInt("ANO");
                    dto.posterUrl = rs.getString("POSTER_URL");
                    dto.texto = rs.getString("TEXTO");
                    BigDecimal n = rs.getBigDecimal("NOTA");
                    dto.nota = (n != null) ? n.doubleValue() : null;
                    Date d = rs.getDate("DATA_PUBLICACAO");
                    dto.dataPublicacao = (d != null) ? d.toLocalDate() : LocalDate.now();
                    lista.add(dto);
                }
            }
        }
        return lista;
    }

    public void salvar(int idUsuario, int idFilme, String texto) throws SQLException {
        String sql = "INSERT INTO RESENHA (ID_USUARIO, ID_FILME, TEXTO, DATA_PUBLICACAO) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idFilme);
            ps.setString(3, texto);
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
        }
    }

    public void atualizar(int idResenha, int idUsuario, String texto) throws SQLException {
        String sql = "UPDATE RESENHA SET TEXTO = ? WHERE ID_RESENHA = ? AND ID_USUARIO = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, texto);
            ps.setInt(2, idResenha);
            ps.setInt(3, idUsuario);
            ps.executeUpdate();
        }
    }

    public void excluir(int idResenha, int idUsuario) throws SQLException {
        String sql = "DELETE FROM RESENHA WHERE ID_RESENHA = ? AND ID_USUARIO = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idResenha);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        }
    }
}