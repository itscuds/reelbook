package br.com.reelbook.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class CatalogoInicializador {

    private CatalogoInicializador() {
    }

    public static void inicializar() throws SQLException {
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

        int[] resultadoGeneros = garantirGeneros(generos);
        int generosCriados = resultadoGeneros[0];
        int falhasGeneros = resultadoGeneros[1];
        int inseridos = 0;
        int existentes = 0;
        int falhas = 0;

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
                },
                {
                        "A Viagem de Chihiro",
                        "Uma menina entra em um mundo habitado por espíritos e precisa encontrar coragem para salvar os pais e retornar para casa.",
                        2001, 125,
                        "https://image.tmdb.org/t/p/w500/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg",
                        new String[]{"Animação", "Aventura", "Fantasia"}
                },
                {
                        "Meu Amigo Totoro",
                        "Duas irmãs se mudam para o campo e descobrem criaturas mágicas que habitam a floresta ao redor de sua nova casa.",
                        1988, 86,
                        "https://image.tmdb.org/t/p/w500/rtGDOeG9LzoerkDGZF9dnVeLppL.jpg",
                        new String[]{"Animação", "Aventura", "Fantasia"}
                },
                {
                        "Princesa Mononoke",
                        "Um jovem guerreiro se envolve no conflito entre os habitantes de uma cidade mineradora e os espíritos da floresta.",
                        1997, 134,
                        "https://image.tmdb.org/t/p/w500/cMYCDADoLKLbB83g4WnJegaZimC.jpg",
                        new String[]{"Animação", "Aventura", "Fantasia"}
                },
                {
                        "O Castelo Animado",
                        "Uma jovem é transformada em idosa por uma bruxa e encontra abrigo no castelo mágico de um misterioso feiticeiro.",
                        2004, 119,
                        "https://image.tmdb.org/t/p/w500/23hUJh7JdO23SpgUB5oiFDQk2wX.jpg",
                        new String[]{"Animação", "Aventura", "Fantasia", "Romance"}
                },
                {
                        "Ponyo: Uma Amizade que Veio do Mar",
                        "Um garoto encontra uma pequena criatura marinha que deseja se tornar humana, provocando mudanças extraordinárias no equilíbrio da natureza.",
                        2008, 100,
                        "https://image.tmdb.org/t/p/w500/yp8vEZflGynlEylxEesbYasc06i.jpg",
                        new String[]{"Animação", "Aventura", "Fantasia"}
                },
                {
                        "O Menino e a Garça",
                        "Após perder a mãe durante a guerra, um garoto se muda para o campo e segue uma garça que o conduz a um mundo fantástico.",
                        2023, 124,
                        "https://image.tmdb.org/t/p/w500/f4oZTcfGrVTXKTWg157AwikXqmP.jpg",
                        new String[]{"Animação", "Aventura", "Fantasia", "Drama"}
                },
                {
                        "O Poderoso Chefão",
                        "O patriarca de uma poderosa família mafiosa transfere gradualmente o controle dos negócios ao filho mais novo, que desejava manter distância do crime.",
                        1972, 175,
                        "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
                        new String[]{"Crime", "Drama"}
                },
                {
                        "Pulp Fiction: Tempo de Violência",
                        "Histórias de criminosos, um boxeador e um casal de assaltantes se cruzam em uma narrativa não linear ambientada em Los Angeles.",
                        1994, 154,
                        "https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg",
                        new String[]{"Crime", "Drama", "Suspense"}
                },
                {
                        "Um Sonho de Liberdade",
                        "Condenado à prisão perpétua por um crime que afirma não ter cometido, um banqueiro constrói uma amizade improvável e encontra esperança dentro da prisão.",
                        1994, 142,
                        "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
                        new String[]{"Drama", "Crime"}
                },
                {
                        "Parasita",
                        "Uma família em dificuldades financeiras encontra uma oportunidade de mudar de vida ao se infiltrar gradualmente na rotina de uma família rica.",
                        2019, 132,
                        "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
                        new String[]{"Drama", "Suspense", "Comédia"}
                },
                {
                        "2001: Uma Odisseia no Espaço",
                        "Uma missão espacial rumo a Júpiter revela os mistérios de um artefato enigmático e os limites da inteligência humana.",
                        1968, 149,
                        "https://image.tmdb.org/t/p/w500/ve72VxNqjGM69Uky4WTo2bK6rfq.jpg",
                        new String[]{"Ficção científica", "Aventura"}
                },
                {
                        "Blade Runner: O Caçador de Androides",
                        "Em um futuro sombrio, um caçador de replicantes recebe a missão de localizar androides fugitivos que se parecem cada vez mais com humanos.",
                        1982, 118,
                        "https://image.tmdb.org/t/p/w500/63N9uy8nd9j7Eog2axPQ8lbr3Wj.jpg",
                        new String[]{"Ficção científica", "Drama", "Suspense"}
                },
                {
                        "O Iluminado",
                        "Um escritor aceita trabalhar como zelador de um hotel isolado durante o inverno, mas forças sobrenaturais e seus próprios conflitos ameaçam sua família.",
                        1980, 144,
                        "https://image.tmdb.org/t/p/w500/xazWoLealQwEgqZ89MLZklLZD3k.jpg",
                        new String[]{"Terror", "Suspense"}
                },
                {
                        "Taxi Driver",
                        "Um veterano de guerra que trabalha como taxista durante a madrugada desenvolve uma visão cada vez mais perturbadora da cidade e de seus habitantes.",
                        1976, 114,
                        "https://image.tmdb.org/t/p/w500/ekstpH614fwDX8DUln1a2Opz0N8.jpg",
                        new String[]{"Drama", "Crime"}
                },
                {
                        "Os Bons Companheiros",
                        "A ascensão e a queda de um jovem envolvido com a máfia são narradas através de décadas de crimes, ambição e traições.",
                        1990, 145,
                        "https://image.tmdb.org/t/p/w500/aKuFiU82s5ISJpGZp7YkIr3kCUd.jpg",
                        new String[]{"Crime", "Drama"}
                },
                {
                        "Janela Indiscreta",
                        "Imobilizado em seu apartamento, um fotógrafo passa a observar os vizinhos e suspeita que um deles tenha cometido um assassinato.",
                        1954, 112,
                        "https://image.tmdb.org/t/p/w500/qitnZcLP7C9DLRuPpmvZ7GiEjJN.jpg",
                        new String[]{"Suspense"}
                },
                {
                        "Brilho Eterno de uma Mente sem Lembranças",
                        "Após descobrir que sua ex-companheira apagou as lembranças do relacionamento, um homem decide passar pelo mesmo procedimento.",
                        2004, 108,
                        "https://image.tmdb.org/t/p/w500/5MwkWH9tYHv3mV9OdYTMR5qreIz.jpg",
                        new String[]{"Drama", "Romance", "Ficção científica"}
                },
                {
                        "O Fabuloso Destino de Amélie Poulain",
                        "Uma jovem garçonete parisiense decide transformar discretamente a vida das pessoas ao seu redor enquanto tenta encontrar sua própria felicidade.",
                        2001, 122,
                        "https://image.tmdb.org/t/p/w500/oTKduWL2tpIKEmkAqF4mFEAWAsv.jpg",
                        new String[]{"Comédia", "Romance"}
                },
                {
                        "Cidade de Deus",
                        "Dois jovens seguem caminhos opostos em uma comunidade do Rio de Janeiro marcada pela violência e pelo crescimento do tráfico.",
                        2002, 130,
                        "https://image.tmdb.org/t/p/w500/k7eYdWvhYQyRQoU2TB2A2Xu2TfD.jpg",
                        new String[]{"Crime", "Drama"}
                },
                {
                        "O Segredo de Brokeback Mountain",
                        "Dois jovens trabalhadores desenvolvem uma relação amorosa durante um verão nas montanhas de Wyoming e passam décadas tentando conciliar seus sentimentos com as expectativas sociais.",
                        2005, 134,
                        "https://image.tmdb.org/t/p/w500/aByfQOQBNa4CMFwIgq3QrqY2ZHh.jpg",
                        new String[]{"Drama", "Romance"}
                },
                {
                        "Donnie Darko",
                        "Um adolescente sobrevive a um acidente inexplicável e passa a receber mensagens de uma figura misteriosa que prevê o fim do mundo.",
                        2001, 114,
                        "https://image.tmdb.org/t/p/w500/fhQoQfejY1hUcwyuLgpBrYs6uFt.jpg",
                        new String[]{"Drama", "Ficção científica", "Suspense"}
                },
                {
                        "Oldboy",
                        "Após passar quinze anos sequestrado sem explicação, um homem é libertado e inicia uma busca obsessiva por respostas e vingança.",
                        2003, 120,
                        "https://image.tmdb.org/t/p/w500/pWDtjs568ZfOTMbURQBYuT4Qxka.jpg",
                        new String[]{"Drama", "Suspense", "Ação"}
                },
                {
                        "Cinema Paradiso",
                        "Um cineasta relembra a infância em uma pequena cidade italiana e a amizade que desenvolveu com o projecionista do cinema local.",
                        1988, 124,
                        "https://image.tmdb.org/t/p/w500/8SRUfRUi6x4O68n0VCbDNRa6iGL.jpg",
                        new String[]{"Drama", "Romance"}
                },
                {
                        "O Grande Lebowski",
                        "Um homem tranquilo conhecido como Dude é confundido com um milionário e acaba envolvido em uma trama absurda de sequestro e extorsão.",
                        1998, 117,
                        "https://image.tmdb.org/t/p/w500/3bv6WAp6BSxxYvB5ozKFUYuRA8C.jpg",
                        new String[]{"Comédia", "Crime"}
                },
                {
                        "O Grande Hotel Budapeste",
                        "As aventuras de um concierge lendário e seu jovem protegido se desenrolam em meio a roubos, heranças e mudanças políticas na Europa.",
                        2014, 100,
                        "https://image.tmdb.org/t/p/w500/eWdyYQreja6JGCzqHWXpWHDrrPo.jpg",
                        new String[]{"Comédia", "Aventura", "Drama"}
                },
                {
                        "Whiplash: Em Busca da Perfeição",
                        "Um jovem baterista entra em um conservatório de elite e enfrenta um instrutor cuja exigência extrema transforma a busca pela excelência em obsessão.",
                        2014, 107,
                        "https://image.tmdb.org/t/p/w500/7fn624j5lj3xTme2SgiLCeuedmO.jpg",
                        new String[]{"Drama"}
                },
                {
                        "O Show de Truman",
                        "Um homem começa a desconfiar de sua realidade perfeita e descobre sinais de que sua vida pode estar sendo observada por milhões de pessoas.",
                        1998, 103,
                        "https://image.tmdb.org/t/p/w500/vuza0WqY239yBXOadKlGwJsZJFE.jpg",
                        new String[]{"Comédia", "Drama", "Ficção científica"}
                },
                {
                        "A Rede Social",
                        "A criação de uma rede social universitária transforma a vida de um estudante e desencadeia disputas sobre amizade, poder e propriedade intelectual.",
                        2010, 120,
                        "https://image.tmdb.org/t/p/w500/n0ybibhJtQ5icDqTp8eRytcIHJx.jpg",
                        new String[]{"Drama"}
                },
                {
                        "O Enigma de Outro Mundo",
                        "Uma equipe científica isolada na Antártida enfrenta uma criatura extraterrestre capaz de imitar qualquer ser vivo.",
                        1982, 109,
                        "https://image.tmdb.org/t/p/w500/tzGY49kseSE9QAKk47uuDGwnSCu.jpg",
                        new String[]{"Terror", "Ficção científica", "Suspense"}
                },
                {
                        "Alien: O Oitavo Passageiro",
                        "A tripulação de uma nave comercial responde a um sinal desconhecido e acaba enfrentando uma criatura mortal a bordo.",
                        1979, 117,
                        "https://image.tmdb.org/t/p/w500/vfrQk5IPloGg1v9Rzbh2Eg3VGyM.jpg",
                        new String[]{"Terror", "Ficção científica", "Suspense"}
                },
                {
                        "O Profissional",
                        "Um assassino profissional solitário passa a proteger uma menina cuja família foi assassinada por um agente corrupto.",
                        1994, 111,
                        "https://image.tmdb.org/t/p/w500/yI6X2cCM5YPJtxMhUd3dPGqDAhw.jpg",
                        new String[]{"Ação", "Crime", "Drama"}
                },
                {
                        "Três Homens em Conflito",
                        "Durante a Guerra Civil Americana, três pistoleiros rivais procuram um tesouro enterrado enquanto disputam entre si.",
                        1966, 161,
                        "https://image.tmdb.org/t/p/w500/bX2xnavhMYjWDoZp1VM6VnU1xwe.jpg",
                        new String[]{"Aventura", "Drama", "Ação"}
                },
                {
                        "Nascido para Matar",
                        "Um grupo de recrutas enfrenta o treinamento brutal dos fuzileiros navais e, depois, a violência da Guerra do Vietnã.",
                        1987, 116,
                        "https://image.tmdb.org/t/p/w500/kMKyx1k8hWWscYFnPbnxxN4Eqo4.jpg",
                        new String[]{"Drama", "História"}
                },
                {
                        "La La Land: Cantando Estações",
                        "Uma atriz em busca de reconhecimento e um músico apaixonado por jazz se aproximam enquanto perseguem seus sonhos em Los Angeles.",
                        2016, 128,
                        "https://image.tmdb.org/t/p/w500/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg",
                        new String[]{"Drama", "Romance", "Comédia"}
                },
                {
                        "O Pianista",
                        "Durante a ocupação nazista de Varsóvia, um pianista judeu luta para sobreviver enquanto perde sua família, sua casa e quase tudo o que conhecia.",
                        2002, 150,
                        "https://image.tmdb.org/t/p/w500/2hFvxCCWrTmCYwfy7yum0GKRi3Y.jpg",
                        new String[]{"Drama", "História"}
                }
        };

        try (Connection c = ConnectionFactory.getConnection()) {
            for (Object[] filme : filmes) {
                try {
                    boolean novo = salvarFilme(c, filme);
                    if (novo) {
                        inseridos++;
                    } else {
                        existentes++;
                    }
                } catch (SQLException e) {
                    falhas++;
                    System.err.println("[ReelBook] Falha ao sincronizar o filme '" + filme[0] + "': " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        System.out.println("[ReelBook] Catálogo conferido: "
                + inseridos + " inserido(s), "
                + existentes + " já existente(s), "
                + falhas + " falha(s) em filmes. "
                + generosCriados + " gênero(s) criado(s), "
                + falhasGeneros + " falha(s) em gêneros.");

        if (falhas > 0 || falhasGeneros > 0) {
            System.err.println("[ReelBook] Consulte os erros acima para identificar os itens que não foram sincronizados.");
        }
    }

    private static int[] garantirGeneros(String[][] generos) throws SQLException {
        int criados = 0;
        int falhas = 0;

        try (Connection c = ConnectionFactory.getConnection()) {
            for (String[] genero : generos) {
                c.setAutoCommit(false);
                try {
                    try (PreparedStatement consulta = c.prepareStatement(
                            "SELECT ID_GENERO FROM GENERO WHERE NOME = ?")) {
                        consulta.setString(1, genero[0]);
                        try (ResultSet rs = consulta.executeQuery()) {
                            if (!rs.next()) {
                                try (PreparedStatement insercao = c.prepareStatement(
                                        "INSERT INTO GENERO (NOME, DESCRICAO, ORDEM, ATIVO) VALUES (?, ?, ?, TRUE)")) {
                                    insercao.setString(1, genero[0]);
                                    insercao.setString(2, "Filmes de " + genero[0].toLowerCase());
                                    insercao.setInt(3, Integer.parseInt(genero[1]));
                                    insercao.executeUpdate();
                                }
                                criados++;
                            }
                        }
                    }
                    c.commit();
                } catch (SQLException e) {
                    c.rollback();
                    falhas++;
                    System.err.println("[ReelBook] Não foi possível garantir o gênero '" + genero[0] + "': " + e.getMessage());
                } finally {
                    c.setAutoCommit(true);
                }
            }
        }

        return new int[]{criados, falhas};
    }


    private static boolean salvarFilme(Connection c, Object[] filme) throws SQLException {
        c.setAutoCommit(false);

        try {
            String titulo = (String) filme[0];
            int idFilme = buscarFilme(c, titulo);
            boolean novo = idFilme == 0;

            if (novo) {
                // Insere o filme quando ainda não existe no banco.
                try (PreparedStatement ins = c.prepareStatement(
                        "INSERT INTO FILME (TITULO, SINOPSE, ANO, DURACAO, POSTER_URL, ATIVO) "
                                + "VALUES (?, ?, ?, ?, ?, TRUE)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {

                    ins.setString(1, titulo);
                    ins.setString(2, (String) filme[1]);
                    ins.setInt(3, (Integer) filme[2]);
                    ins.setInt(4, (Integer) filme[3]);
                    ins.setString(5, (String) filme[4]);

                    ins.executeUpdate();

                    try (ResultSet keys = ins.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException(
                                    "O banco não retornou o ID do filme '" + titulo + "'."
                            );
                        }

                        idFilme = keys.getInt(1);
                    }
                }

            } else {
                // Atualiza o pôster quando o filme já existe no banco.
                try (PreparedStatement atualizar = c.prepareStatement(
                        "UPDATE FILME SET POSTER_URL = ? WHERE ID_FILME = ?")) {

                    atualizar.setString(1, (String) filme[4]);
                    atualizar.setInt(2, idFilme);
                    atualizar.executeUpdate();
                }
            }

            // Garante que todos os gêneros estejam associados ao filme.
            String[] nomesGeneros = (String[]) filme[5];

            for (String nomeGenero : nomesGeneros) {
                int idGenero = buscarGenero(c, nomeGenero);

                if (idGenero == 0) {
                    throw new SQLException("Gênero não encontrado: " + nomeGenero);
                }

                try (PreparedStatement relacao = c.prepareStatement(
                        "INSERT INTO FILME_GENERO (ID_FILME, ID_GENERO) "
                                + "SELECT ?, ? WHERE NOT EXISTS "
                                + "(SELECT 1 FROM FILME_GENERO "
                                + "WHERE ID_FILME = ? AND ID_GENERO = ?)")) {

                    relacao.setInt(1, idFilme);
                    relacao.setInt(2, idGenero);
                    relacao.setInt(3, idFilme);
                    relacao.setInt(4, idGenero);

                    relacao.executeUpdate();
                }
            }

            c.commit();
            return novo;

        } catch (SQLException e) {
            c.rollback();
            throw e;

        } finally {
            c.setAutoCommit(true);
        }
    }


    private static int buscarFilme(Connection c, String titulo) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT ID_FILME FROM FILME WHERE TITULO = ?")) {
            ps.setString(1, titulo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private static int buscarGenero(Connection c, String nome) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT ID_GENERO FROM GENERO WHERE NOME = ?")) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}