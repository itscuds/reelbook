<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <title>ReelBook — Explorar filmes</title>

  <link
    rel="icon"
    href="<%= request.getContextPath() %>/assets/images/logo-icon.png"
  />

  <link
    rel="stylesheet"
    href="<%= request.getContextPath() %>/css/style.css"
  />

  <link
    rel="stylesheet"
    href="<%= request.getContextPath() %>/css/components.css"
  />
</head>

<body
  data-page="explorar"
  data-basepath="<%= request.getContextPath() %>/"
>

  <header id="app-navbar" class="navbar container"></header>

  <main class="container section">

    <h1>Explorar filmes</h1>

    <form id="filtros-filmes" class="toolbar">

      <input
        id="busca-filme"
        type="search"
        class="search-input"
        placeholder="Buscar por título..."
      />

      <div class="toolbar__filters">

        <select id="filtro-genero">
          <option value="">Todos os gêneros</option>
        </select>

        <select id="filtro-ano">
          <option value="">Todos os anos</option>
        </select>

        <button
          class="btn btn--primary btn--sm"
          type="submit"
        >
          Filtrar
        </button>

        <button
          class="btn btn--outline btn--sm"
          type="reset"
          id="limpar-filtros"
        >
          Limpar
        </button>

      </div>
    </form>

    <p
      id="resultado-contagem"
      class="text-faint"
      aria-live="polite"
    ></p>

    <div
      id="resultados-grid"
      class="movie-grid"
      style="margin-top: 24px"
      aria-live="polite"
    ></div>

  </main>

  <footer class="footer container">
    <p class="text-faint">
      © 2026 ReelBook — Projeto acadêmico.
    </p>
  </footer>

  <script src="<%= request.getContextPath() %>/js/components.js"></script>
  <script src="<%= request.getContextPath() %>/js/main.js"></script>
  <script src="<%= request.getContextPath() %>/js/explorar.js"></script>

</body>
</html>