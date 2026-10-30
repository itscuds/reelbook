package br.com.reelbook.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class CatalogoInicializador {

    private CatalogoInicializador() {
    }

    public static void inicializar() throws SQLException {
        try (Connection c = ConnectionFactory.getConnection()) {
            c.setAutoCommit(false);

            try {
                String[][] generos = {
                        {"Ação", "1"},
                        {"Aventura", "2"},
                        {"Animação", "3"},
                        {"Comédia", "4"},
                        {"Drama", "5"},
                        {"Fantasia", "6"},
                        {"Ficção científica", "7"},
                        {"Romance", "8"},
                        {"Terror", "9"},
                        {"Suspense", "10"},
                        {"História", "11"},
                        {"Crime", "12"}
                };

                for (String[] g : generos) {
                    try (PreparedStatement q = c.prepareStatement(
                            "SELECT ID_GENERO FROM GENERO WHERE NOME = ?")) {

                        q.setString(1, g[0]);

                        try (ResultSet rs = q.executeQuery()) {
                            if (!rs.next()) {
                                try (PreparedStatement ins = c.prepareStatement(
                                        "INSERT INTO GENERO "
                                                + "(NOME, DESCRICAO, ORDEM, ATIVO) "
                                                + "VALUES (?, ?, ?, TRUE)")) {

                                    ins.setString(1, g[0]);
                                    ins.setString(
                                            2,
                                            "Filmes de " + g[0].toLowerCase()
                                    );
                                    ins.setInt(3, Integer.parseInt(g[1]));
                                    ins.executeUpdate();
                                }
                            }
                        }
                    }
                }

                Object[][] filmes = {
                        {
                                "Duna: Parte Dois",
                                "Paul Atreides se une a Chani e aos Fremen enquanto busca vingança contra os conspiradores que destruíram sua família.",
                                2024, 166,
                                "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
                                new String[]{"Ficção científica", "Aventura", "Drama"}
                        },
                        {
                                "Interestelar",
                                "Um grupo de exploradores viaja através de um buraco de minhoca em uma tentativa de garantir a sobrevivência da humanidade.",
                                2014, 169,
                                "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
                                new String[]{"Ficção científica", "Drama", "Aventura"}
                        },
                        {
                                "Pobres Criaturas",
                                "Uma jovem é trazida de volta à vida por um cientista excêntrico e embarca em uma jornada de descoberta e liberdade.",
                                2023, 141,
                                "https://image.tmdb.org/t/p/original/9OYMVcP2zyw0zpWOTuxlDo2MsMw.jpg",
                                new String[]{"Comédia", "Drama", "Ficção científica"}
                        },
                        {
                                "Oppenheimer",
                                "A história de J. Robert Oppenheimer e seu papel no desenvolvimento da primeira bomba atômica.",
                                2023, 181,
                                "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                                new String[]{"Drama", "História"}
                        },
                        {
                                "Barbie",
                                "Barbie deixa Barbieland e parte para o mundo real em uma jornada que transforma sua visão sobre a própria existência.",
                                2023, 114,
                                "https://image.tmdb.org/t/p/w500/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg",
                                new String[]{"Comédia", "Aventura", "Fantasia"}
                        },
                        {
                                "Clube da Luta",
                                "Um homem insatisfeito com sua vida conhece Tyler Durden e acaba envolvido em um clube clandestino de luta.",
                                1999, 139,
                                "https://image.tmdb.org/t/p/w500/bptfVGEQuv6vDTIMVCHjJ9Dz8PX.jpg",
                                new String[]{"Drama"}
                        },
                        {
                                "A Origem",
                                "Um especialista em extrair informações através dos sonhos recebe a missão de implantar uma ideia na mente de um alvo.",
                                2010, 148,
                                "https://image.tmdb.org/t/p/w500/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg",
                                new String[]{"Ação", "Ficção científica", "Aventura"}
                        },
                        {
                                "Forrest Gump",
                                "Forrest Gump atravessa décadas da história americana enquanto vive uma vida extraordinária sem deixar de lado seu jeito simples de enxergar o mundo.",
                                1994, 142,
                                "https://image.tmdb.org/t/p/w500/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg",
                                new String[]{"Comédia", "Drama", "Romance"}
                        },
                        {
                                "Batman: O Cavaleiro das Trevas",
                                "Batman enfrenta uma ameaça criminosa que mergulha Gotham em um caos cada vez maior.",
                                2008, 152,
                                "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                                new String[]{"Drama", "Ação", "Crime"}
                        },
                        {
                                "Matrix",
                                "Um programador descobre que a realidade como conhece é uma simulação e se junta à resistência contra as máquinas.",
                                1999, 136,
                                "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
                                new String[]{"Ação", "Ficção científica"}
                        }
                };

                for (Object[] f : filmes) {
                    int idFilme = buscarFilme(c, (String) f[0]);

                    if (idFilme == 0) {
                        try (PreparedStatement ins = c.prepareStatement(
                                "INSERT INTO FILME "
                                        + "(TITULO, SINOPSE, ANO, DURACAO, POSTER_URL, ATIVO) "
                                        + "VALUES (?, ?, ?, ?, ?, TRUE)",
                                PreparedStatement.RETURN_GENERATED_KEYS)) {

                            ins.setString(1, (String) f[0]);
                            ins.setString(2, (String) f[1]);
                            ins.setInt(3, (Integer) f[2]);
                            ins.setInt(4, (Integer) f[3]);
                            ins.setString(5, (String) f[4]);
                            ins.executeUpdate();

                            try (ResultSet keys = ins.getGeneratedKeys()) {
                                if (!keys.next()) {
                                    throw new SQLException(
                                            "Falha ao criar filme: " + f[0]
                                    );
                                }

                                idFilme = keys.getInt(1);
                            }
                        }
                    }

                    String[] nomes = (String[]) f[5];

                    for (String nomeGenero : nomes) {
                        int idGenero = buscarGenero(c, nomeGenero);

                        if (idGenero > 0) {
                            try (PreparedStatement rel = c.prepareStatement(
                                    "INSERT INTO FILME_GENERO "
                                            + "(ID_FILME, ID_GENERO) "
                                            + "SELECT ?, ? WHERE NOT EXISTS "
                                            + "(SELECT 1 FROM FILME_GENERO "
                                            + "WHERE ID_FILME = ? AND ID_GENERO = ?)")) {

                                rel.setInt(1, idFilme);
                                rel.setInt(2, idGenero);
                                rel.setInt(3, idFilme);
                                rel.setInt(4, idGenero);
                                rel.executeUpdate();
                            }
                        }
                    }
                }

                c.commit();

            } catch (SQLException e) {
                c.rollback();
                throw e;

            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    private static int buscarFilme(
            Connection c,
            String titulo
    ) throws SQLException {

        try (PreparedStatement ps = c.prepareStatement(
                "SELECT ID_FILME FROM FILME WHERE TITULO = ?")) {

            ps.setString(1, titulo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private static int buscarGenero(
            Connection c,
            String nome
    ) throws SQLException {

        try (PreparedStatement ps = c.prepareStatement(
                "SELECT ID_GENERO FROM GENERO WHERE NOME = ?")) {

            ps.setString(1, nome);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}