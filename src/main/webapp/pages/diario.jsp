
<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>

<%@ page import="br.com.reelbook.dao.DiarioDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>

<%
    List<DiarioDAO.DiarioDTO> diario =
        (List<DiarioDAO.DiarioDTO>) request.getAttribute("diario");

    if (diario == null) {
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
            if (diario.isEmpty()) {
        %>

            <p class="text-muted">
                Seu diário ainda está vazio. Quando marcar um filme
                como assistido, ele aparecerá aqui.
            </p>

        <%
            } else {

                for (DiarioDAO.DiarioDTO item : diario) {

                    String titulo = item.titulo;

                    String comentario = item.comentario;

                    String nota = item.nota == null
                            ? "Sem nota"
                            : BigDecimal.valueOf(item.nota)
                                    .stripTrailingZeros()
                                    .toPlainString();

                    String data = item.dataRegistro == null
                            ? "Data não informada"
                            : item.dataRegistro.toString();
        %>

        <article class="diario-item">

            <img class="diario-item__poster"
                 src="<%= item.posterUrl == null
                     ? "" : item.posterUrl %>"
                 alt="Pôster de <%= titulo %>"
                 loading="lazy" />

            <div class="diario-item__info">

                <p class="diario-item__titulo">
                    <%= titulo %>

                    <span class="text-faint">
                        (<%= item.ano %>)
                    </span>
                </p>

                <p class="movie-card__rating">
                    ★ <%= nota %>
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
                           value="<%= item.idFilme %>" />

                    <input type="hidden"
                           name="origem"
                           value="diario" />

                    <button type="submit"
                            class="btn btn--outline btn--sm"
                            onclick="return confirm('Deseja remover este registro?')">
                        Excluir
                    </button>

                </form>

            </div>

            <span class="text-faint">
                <%= data %>
            </span>

        </article>

        <%
                }
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
