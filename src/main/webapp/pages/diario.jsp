
<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ page import="br.com.reelbook.model.RegistroPessoalDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="br.com.reelbook.util.ConnectionFactory" %>

<%
    List<RegistroPessoalDTO> registros =
        (List<RegistroPessoalDTO>) request.getAttribute("registros");

    if (registros == null) {
        response.sendRedirect(request.getContextPath()
                + "/minha-lista?tipo=diario");
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <title>ReelBook — Diário</title>

    <link rel="icon" href="../assets/images/logo-icon.png" />
    <link rel="stylesheet" href="../css/style.css" />
    <link rel="stylesheet" href="../css/components.css" />
</head>

<body data-page="diario" data-basepath="../">

<header id="app-navbar" class="navbar container"></header>

<main id="main-conteudo" class="container section">

    <h1>Diário</h1>

    <div class="diario-lista">

        <%
            boolean encontrou = false;

            for (RegistroPessoalDTO item : registros) {

                if (item.getRegistro().getStatus() == null
                        || !"ASSISTIDO".equalsIgnoreCase(
                            item.getRegistro().getStatus())) {
                    continue;
                }

                encontrou = true;

                int idFilme = item.getFilme().getIdFilme();
                String comentario = null;

                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;

                try {
                    conn = ConnectionFactory.getConnection();

                    ps = conn.prepareStatement(
                        "SELECT TEXTO FROM RESENHA "
                        + "WHERE ID_USUARIO = ? AND ID_FILME = ?"
                    );

                    ps.setInt(1, item.getRegistro().getIdUsuario());
                    ps.setInt(2, idFilme);

                    rs = ps.executeQuery();

                    if (rs.next()) {
                        comentario = rs.getString("TEXTO");
                    }

                } catch (Exception e) {
                    application.log(
                        "Erro ao carregar comentário do diário", e
                    );

                } finally {

                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception e) {
                        application.log("Erro ao fechar ResultSet", e);
                    }

                    try {
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (Exception e) {
                        application.log(
                            "Erro ao fechar PreparedStatement", e
                        );
                    }

                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (Exception e) {
                        application.log("Erro ao fechar Connection", e);
                    }
                }

                String titulo = item.getFilme().getTitulo();

                BigDecimal nota = item.getRegistro().getNota();

                java.time.LocalDate data =
                    item.getRegistro().getDataRegistro();
        %>

        <article class="diario-item">

            <img class="diario-item__poster"
                 src="<%= item.getFilme().getPosterUrl() == null
                     ? "" : item.getFilme().getPosterUrl() %>"
                 alt="Pôster de <%= titulo %>"
                 loading="lazy" />

            <div class="diario-item__info">

                <p class="diario-item__titulo">
                    <%= titulo %>

                    <span class="text-faint">
                        (<%= item.getFilme().getAno() %>)
                    </span>
                </p>

                <p class="movie-card__rating">
                    ★ <%= nota == null
                        ? "Sem nota"
                        : nota.stripTrailingZeros().toPlainString() %>
                </p>

                <p class="text-muted">
                    <%= comentario == null ? "" : comentario %>
                </p>

                <form action="<%= request.getContextPath() %>/registro-pessoal"
                      method="post">

                    <input type="hidden"
                           name="acao"
                           value="remover" />

                    <input type="hidden"
                           name="idFilme"
                           value="<%= idFilme %>" />

                    <button type="submit"
                            class="btn btn--outline btn--sm"
                            onclick="return confirm('Deseja remover este registro?')">
                        Excluir
                    </button>

                </form>

            </div>

            <span class="text-faint">
                <%= data == null
                    ? "Data não informada"
                    : data %>
            </span>

        </article>

        <%
            }

            if (!encontrou) {
        %>

            <p class="text-muted">
                Seu diário ainda está vazio. Quando marcar um filme
                como assistido, ele aparecerá aqui.
            </p>

        <%
            }
        %>

    </div>

</main>

<footer class="footer container">
    <p class="text-faint">
        © 2026 ReelBook — Projeto acadêmico.
    </p>
</footer>

<script src="../js/components.js"></script>
<script src="../js/main.js"></script>

</body>
</html>
