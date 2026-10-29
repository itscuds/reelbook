<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
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

        <div id="diario-lista" class="diario-lista">

            <%
                java.util.List<br.com.reelbook.model.RegistroPessoalDTO> registros =
                    (java.util.List<br.com.reelbook.model.RegistroPessoalDTO>)
                    request.getAttribute("registros");

                boolean encontrou = false;

                if (registros != null) {
                    for (br.com.reelbook.model.RegistroPessoalDTO item : registros) {
                        String status = item.getRegistro().getStatus();

                        if (status == null || !"ASSISTIDO".equalsIgnoreCase(status)) {
                            continue;
                        }

                        encontrou = true;

                        br.com.reelbook.model.Filme filme = item.getFilme();
                        java.math.BigDecimal nota = item.getRegistro().getNota();
                        java.time.LocalDate data = item.getRegistro().getDataRegistro();
            %>

            <article class="diario-item">

                <img
                    class="diario-item__poster"
                    src="<%= filme.getPosterUrl() == null ? "" : filme.getPosterUrl() %>"
                    alt="Pôster de <%= filme.getTitulo() %>"
                    loading="lazy"
                />

                <div class="diario-item__info">

                    <p class="diario-item__titulo">
                        <%= filme.getTitulo() %>
                        <span class="text-faint">(<%= filme.getAno() %>)</span>
                    </p>

                    <% if (nota != null) { %>
                        <p class="movie-card__rating">
                            ★ <%= nota.stripTrailingZeros().toPlainString() %>
                        </p>
                    <% } %>

                    <p class="text-muted">
                        Filme marcado como assistido.
                    </p>

                </div>

                <span class="text-faint">
                    <%= data == null ? "Data não informada" : data.toString() %>
                </span>

            </article>

            <%
                    }
                }

                if (!encontrou) {
            %>

                <p class="text-muted">
                    Seu diário ainda está vazio. Quando você marcar um filme como assistido, ele aparecerá aqui.
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

</body>
</html>
