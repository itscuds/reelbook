
package br.com.reelbook.dao;

import br.com.reelbook.model.RegistroPessoal;
import br.com.reelbook.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegistroPessoalDAO {

    public void adicionar(RegistroPessoal registro) throws SQLException {
        String sql = "INSERT INTO REGISTRO_PESSOAL "
        + "(ID_USUARIO, ID_FILME, STATUS, FAVORITO, NOTA, DATA_REGISTRO) "
        + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, registro.getIdUsuario());
            comando.setInt(2, registro.getIdFilme());
            comando.setString(3, registro.getStatus());
            comando.setBoolean(4, registro.isFavorito());

            if (registro.getNota() == null) {
                comando.setNull(5, java.sql.Types.DECIMAL);
            } else {
                comando.setBigDecimal(5, registro.getNota());
            }

            LocalDate data = registro.getDataRegistro() == null
                    ? LocalDate.now()
                    : registro.getDataRegistro();

            comando.setDate(6, Date.valueOf(data));
            comando.executeUpdate();
        }
    }

    public List<RegistroPessoal> listarPorUsuario(int idUsuario)
            throws SQLException {

        String sql = "SELECT ID_USUARIO, ID_FILME, STATUS, FAVORITO, NOTA, DATA_REGISTRO "
        + "FROM REGISTRO_PESSOAL "
        + "WHERE ID_USUARIO = ? "
        + "ORDER BY DATA_REGISTRO DESC";

        List<RegistroPessoal> registros = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idUsuario);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    RegistroPessoal registro = new RegistroPessoal();

                    registro.setIdUsuario(resultado.getInt("ID_USUARIO"));
                    registro.setIdFilme(resultado.getInt("ID_FILME"));
                    registro.setStatus(resultado.getString("STATUS"));
                    registro.setFavorito(resultado.getBoolean("FAVORITO"));
                    registro.setNota(resultado.getBigDecimal("NOTA"));

                    Date data = resultado.getDate("DATA_REGISTRO");
                    if (data != null) {
                        registro.setDataRegistro(data.toLocalDate());
                    }

                    registros.add(registro);
                }
            }
        }

        return registros;
    }

    public void atualizar(RegistroPessoal registro) throws SQLException {
        String sql = "UPDATE REGISTRO_PESSOAL "
        + "SET STATUS = ?, FAVORITO = ?, NOTA = ? "
        + "WHERE ID_USUARIO = ? AND ID_FILME = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, registro.getStatus());
            comando.setBoolean(2, registro.isFavorito());

            if (registro.getNota() == null) {
                comando.setNull(3, java.sql.Types.DECIMAL);
            } else {
                comando.setBigDecimal(3, registro.getNota());
            }

            comando.setInt(4, registro.getIdUsuario());
            comando.setInt(5, registro.getIdFilme());

            comando.executeUpdate();
        }
    }

    public void remover(int idUsuario, int idFilme) throws SQLException {
        String sql = "DELETE FROM REGISTRO_PESSOAL "
        + "WHERE ID_USUARIO = ? AND ID_FILME = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idUsuario);
            comando.setInt(2, idFilme);

            comando.executeUpdate();
        }
    }

    
public java.util.List<br.com.reelbook.model.RegistroPessoalDTO>
listarComFilmesPorUsuario(int idUsuario) throws java.sql.SQLException {

    java.util.List<br.com.reelbook.model.RegistroPessoalDTO> lista =
            new java.util.ArrayList<>();

    String sql =
        "SELECT f.ID_FILME, f.TITULO, f.SINOPSE, f.ANO, " +
        "f.DURACAO, f.POSTER_URL, f.ATIVO, " +
        "r.ID_USUARIO, r.STATUS, r.FAVORITO, r.NOTA, r.DATA_REGISTRO " +
        "FROM REGISTRO_PESSOAL r " +
        "INNER JOIN FILME f ON f.ID_FILME = r.ID_FILME " +
        "WHERE r.ID_USUARIO = ? " +
        "ORDER BY r.DATA_REGISTRO DESC";

    try (java.sql.Connection conn =
                 br.com.reelbook.util.ConnectionFactory.getConnection();
         java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idUsuario);

        try (java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {

                br.com.reelbook.model.Filme filme =
                        new br.com.reelbook.model.Filme();

                filme.setIdFilme(rs.getInt("ID_FILME"));
                filme.setTitulo(rs.getString("TITULO"));
                filme.setSinopse(rs.getString("SINOPSE"));
                filme.setAno(rs.getInt("ANO"));
                filme.setDuracao(rs.getInt("DURACAO"));
                filme.setPosterUrl(rs.getString("POSTER_URL"));
                filme.setAtivo(rs.getBoolean("ATIVO"));

                br.com.reelbook.model.RegistroPessoal registro =
                        new br.com.reelbook.model.RegistroPessoal();

                registro.setIdUsuario(rs.getInt("ID_USUARIO"));
                registro.setIdFilme(rs.getInt("ID_FILME"));
                registro.setStatus(rs.getString("STATUS"));
                registro.setFavorito(rs.getBoolean("FAVORITO"));

                java.math.BigDecimal nota = rs.getBigDecimal("NOTA");
                registro.setNota(nota);

                java.sql.Date data = rs.getDate("DATA_REGISTRO");
                if (data != null) {
                    registro.setDataRegistro(data.toLocalDate());
                }

                lista.add(
                    new br.com.reelbook.model.RegistroPessoalDTO(
                        filme, registro
                    )
                );
            }
        }
    }

    return lista;
}

}
