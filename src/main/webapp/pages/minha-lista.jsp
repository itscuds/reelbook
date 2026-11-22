
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="br.com.reelbook.model.RegistroPessoalDTO" %>
<%@ page import="java.util.List" %>

<%
    List<RegistroPessoalDTO> registros =
        (List<RegistroPessoalDTO>) request.getAttribute("registros");

    if (registros == null) {
        response.sendRedirect(
            request.getContextPath() + "/minha-lista?tipo=lista"
        );
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>ReelBook — Minha Lista</title>

    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/components.css">
</head>

<body data-page="listas" data-basepath="../">

<header id="app-navbar" class="navbar container"></header>

<main id="main-conteudo" class="container section">

    <h1>Minha Lista</h1>

    <p class="text-muted">
        Filmes que você adicionou à sua coleção pessoal.
    </p>

    <div class="diario-lista">

        <%
            if (registros.isEmpty()) {
        %>

            <p class="text-muted">
                Sua lista ainda está vazia.
                Explore o catálogo e adicione filmes!
            </p>

        <%
            } else {
                for (RegistroPessoalDTO item : registros) {

                    int idFilme = item.getFilme().getIdFilme();
                    String titulo = item.getFilme().getTitulo();
                    String status = item.getRegistro().getStatus();
        %>

        <article class="diario-item">

            <img
                class="diario-item__poster"
                src="<%= item.getFilme().getPosterUrl() == null
                    ? "" : item.getFilme().getPosterUrl() %>"
                alt="Pôster de <%= titulo %>"
                loading="lazy"
            >

            <div class="diario-item__info">

                <p class="diario-item__titulo">
                    <%= titulo %>
                    <span class="text-faint">
                        (<%= item.getFilme().getAno() %>)
                    </span>
                </p>

                <p class="text-muted">
                    Status:
                    <%= "ASSISTIDO".equals(status) ? "Assistido" : "Quero assistir" %>
                    <%= item.getRegistro().isFavorito() ? " · ★ Favorito" : "" %>
                </p>

                <% if ("ASSISTIDO".equals(status)) { %>
                    <p class="movie-card__rating">
                        <%= item.getRegistro().getNota() == null
                            ? "Sem nota"
                            : "★ " + item.getRegistro().getNota() %>
                    </p>
                <% } %>

                <a href="<%= request.getContextPath() %>/filmes?acao=detalhe&id=<%= idFilme %>">
                    Ver detalhes
                </a>

                <!-- Mudar status -->
                <form
                    action="<%= request.getContextPath() %>/registro-pessoal"
                    method="post"
                >
                    <input type="hidden" name="acao" value="atualizar">
                    <input type="hidden" name="idFilme" value="<%= idFilme %>">
                    <input type="hidden" name="origem" value="lista">
                    <input type="hidden" name="status"
                           value="<%= "ASSISTIDO".equals(status) ? "QUERO_ASSISTIR" : "ASSISTIDO" %>">

                    <button type="submit" class="btn btn--outline btn--sm">
                        <%= "ASSISTIDO".equals(status)
                            ? "Voltar para quero assistir"
                            : "Marcar como assistido" %>
                    </button>
                </form>

                <!-- Dar nota (só para filmes assistidos) -->
                <% if ("ASSISTIDO".equals(status)) { %>
                <form
                    action="<%= request.getContextPath() %>/registro-pessoal"
                    method="post"
                >
                    <input type="hidden" name="acao" value="atualizar">
                    <input type="hidden" name="idFilme" value="<%= idFilme %>">
                    <input type="hidden" name="origem" value="lista">

                    <input class="input" type="number" name="nota" min="0" max="5" step="0.5"
                           value="<%= item.getRegistro().getNota() == null
                               ? "" : item.getRegistro().getNota() %>"
                           aria-label="Nota de 0 a 5" required>

                    <button type="submit" class="btn btn--outline btn--sm">
                        Salvar nota
                    </button>
                </form>
                <% } %>

                <!-- Favoritar / desfavoritar -->
                <form
                    action="<%= request.getContextPath() %>/registro-pessoal"
                    method="post"
                >
                    <input type="hidden" name="acao" value="atualizar">
                    <input type="hidden" name="idFilme" value="<%= idFilme %>">
                    <input type="hidden" name="origem" value="lista">
                    <input type="hidden" name="favorito"
                           value="<%= item.getRegistro().isFavorito() ? "false" : "true" %>">

                    <button type="submit" class="btn btn--outline btn--sm">
                        <%= item.getRegistro().isFavorito()
                            ? "Remover dos favoritos"
                            : "Adicionar aos favoritos" %>
                    </button>
                </form>

                <!-- Remover da lista -->
                <form
                    action="<%= request.getContextPath() %>/registro-pessoal"
                    method="post"
                >
                    <input type="hidden" name="acao" value="remover">
                    <input type="hidden" name="idFilme" value="<%= idFilme %>">
                    <input type="hidden" name="origem" value="lista">

                    <button
                        type="submit"
                        class="btn btn--outline btn--sm"
                        onclick="return confirm('Deseja remover este filme da sua lista?')"
                    >
                        Remover
                    </button>
                </form>

            </div>

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
