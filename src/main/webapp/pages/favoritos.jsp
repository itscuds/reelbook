<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>ReelBook — Favoritos</title>
    <link rel="icon" href="../assets/images/logo-icon.png" />
    <link rel="stylesheet" href="../css/style.css" />
    <link rel="stylesheet" href="../css/components.css" />
</head>
<body data-page="listas" data-basepath="../">

    <header id="app-navbar" class="navbar container"></header>

    <main id="main-conteudo" class="container section">
        <h2>Meus favoritos</h2>

        <p class="text-muted" id="favoritos-contador"
           style="margin-top:4px;">
            Carregando favoritos...
        </p>

        <div id="favoritos-grid" class="movie-grid"
             style="margin-top:32px;">

            <%
                java.util.List<br.com.reelbook.model.RegistroPessoalDTO> registros =
                    (java.util.List<br.com.reelbook.model.RegistroPessoalDTO>)
                    request.getAttribute("registros");

                int total = 0;

                if (registros != null) {
                    for (br.com.reelbook.model.RegistroPessoalDTO item : registros) {
                        if (item.getRegistro().isFavorito()) {
                            total++;
                        }
                    }
                }

                if (registros != null && total > 0) {
                    for (br.com.reelbook.model.RegistroPessoalDTO item : registros) {
                        if (!item.getRegistro().isFavorito()) {
                            continue;
                        }

                        br.com.reelbook.model.Filme filme = item.getFilme();
            %>

            <article class="movie-card">
                <a href="<%= request.getContextPath() %>/FilmeServlet?acao=detalhe&id=<%= filme.getIdFilme() %>">
                    <img
                        src="<%= filme.getPosterUrl() == null ? "" : filme.getPosterUrl() %>"
                        alt="Pôster de <%= filme.getTitulo() %>"
                        loading="lazy"
                    />
                    <h3><%= filme.getTitulo() %></h3>
                    <p><%= filme.getAno() %></p>
                </a>

                <form action="<%= request.getContextPath() %>/registro-pessoal"
                      method="post">
                    <input type="hidden" name="acao" value="atualizar" />
                    <input type="hidden" name="idFilme"
                           value="<%= filme.getIdFilme() %>" />
                    <input type="hidden" name="status"
                           value="<%= item.getRegistro().getStatus() == null ? "QUERO_ASSISTIR" : item.getRegistro().getStatus() %>" />
                    <input type="hidden" name="favorito" value="false" />

                    <button type="submit" class="btn btn-secondary"
                            aria-label="Remover dos favoritos">
                        Remover dos favoritos
                    </button>
                </form>
            </article>

            <%
                    }
                } else {
            %>

            <p class="text-muted">
                Você ainda não adicionou filmes aos favoritos.
            </p>

            <%
                }
            %>
        </div>
    </main>

    <footer class="footer container">
        <p class="text-faint">© 2026 ReelBook — Projeto acadêmico.</p>
    </footer>

    <script src="../js/components.js"></script>
    <script src="../js/main.js"></script>

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const contador = document.getElementById('favoritos-contador');
            const grid = document.getElementById('favoritos-grid');

            const quantidade = grid.querySelectorAll('article.movie-card').length;
            contador.textContent =
                quantidade + (quantidade === 1 ? ' filme favorito' : ' filmes favoritos');
        });
    </script>

</body>
</html>
