<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <title>ReelBook — Seu mundo, seus filmes</title>

  <link rel="icon" href="<%= request.getContextPath() %>/assets/images/logo-icon.png" />

  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css" />
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/components.css" />
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/home.css" />
</head>

<body
  data-page="home"
  data-basepath="<%= request.getContextPath() %>/"
>

  <header id="app-navbar" class="navbar container"></header>

  <main>
    <section class="hero container">
      <div class="hero__content">

        <img
          class="hero__logo"
          src="<%= request.getContextPath() %>/assets/images/logo-full.png"
          alt="ReelBook — Explore. Avalie. Relembre."
        />

        <div id="hero-text"></div>

      </div>
    </section>

    <section class="section container">

      <div class="section__header">
        <h2>Em destaque</h2>

        <a href="<%= request.getContextPath() %>/filmes?acao=explorar">
          Ver todos
        </a>
      </div>

      <div
        id="destaque-grid"
        class="movie-grid"
        aria-live="polite"
      ></div>

    </section>
  </main>

  <footer class="footer container">
    <p class="text-faint">
      © 2026 ReelBook — Projeto acadêmico.
    </p>
  </footer>

  <script src="<%= request.getContextPath() %>/js/components.js"></script>
  <script src="<%= request.getContextPath() %>/js/main.js"></script>
  <script src="<%= request.getContextPath() %>/js/home.js"></script>
  <script src="<%= request.getContextPath() %>/js/api.js"></script>

</body>
</html>