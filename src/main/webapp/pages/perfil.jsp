<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="br.com.reelbook.model.Usuario" %>
<%@ page import="br.com.reelbook.dao.UsuarioDAO.PerfilStatsDTO" %>
<%@ page import="br.com.reelbook.dao.ResenhaDAO.ResenhaDTO" %>
<%@ page import="java.util.List" %>
<%
  Usuario usuario = (Usuario) request.getAttribute("usuario");
  if (usuario == null) {
    response.sendRedirect(request.getContextPath() + "/perfil");
    return;
  }
  PerfilStatsDTO stats = (PerfilStatsDTO) request.getAttribute("stats");
  List<ResenhaDTO> resenhas = (List<ResenhaDTO>) request.getAttribute("resenhas");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>ReelBook — Perfil de <%= usuario.getNome() %></title>
  <link rel="icon" href="../assets/images/logo-icon.png" />
  <link rel="stylesheet" href="../css/style.css" />
  <link rel="stylesheet" href="../css/components.css" />
</head>
<body data-page="perfil" data-basepath="../">

  <header id="app-navbar" class="navbar container"></header>

  <main id="main-conteudo" class="container section">
    <div class="perfil-header">
      <img class="perfil-header__avatar" src="https://i.pravatar.cc/160?u=<%= usuario.getId() %>" alt="Foto de perfil" />
      <div>
        <h1 id="perfil-nome"><%= usuario.getNome() %></h1>
        <p class="text-muted"><%= usuario.getEmail() %> • Membro desde <%= usuario.getDataCadastro() %></p>
      </div>
    </div>

    <div class="perfil-stats">
      <div><strong><%= stats != null ? stats.totalAssistidos : 0 %></strong><span class="text-faint">Filmes assistidos</span></div>
      <div><strong><%= stats != null ? stats.totalResenhas : 0 %></strong><span class="text-faint">Avaliações</span></div>
      <div><strong><%= stats != null ? stats.totalFavoritos : 0 %></strong><span class="text-faint">Favoritos</span></div>
    </div>

    <div style="margin-top: var(--space-6);">
      <h2 style="font-size: var(--fs-md); margin-bottom: var(--space-4);">Minhas Avaliações e Resenhas</h2>

      <div class="diario-lista">
        <% if (resenhas == null || resenhas.isEmpty()) { %>
          <div class="empty-state">Você ainda não escreveu nenhuma avaliação.</div>
        <% } else {
            for (ResenhaDTO r : resenhas) {
        %>
          <div class="diario-item">
            <img class="diario-item__poster"
                 src="<%= (r.posterUrl != null && !r.posterUrl.isEmpty()) ? r.posterUrl : "../assets/images/logo-icon.png" %>"
                 alt="<%= r.tituloFilme %>" />
            <div class="diario-item__info">
              <p class="diario-item__titulo"><%= r.tituloFilme %> <span class="text-faint">(<%= r.anoFilme %>)</span></p>
              <p class="movie-card__rating">★ <%= r.nota != null ? String.format("%.1f", r.nota) : "Sem nota" %></p>
              <p class="text-muted" style="margin-top: 6px;"><%= r.texto %></p>
            </div>
            <span class="text-faint"><%= r.dataPublicacao %></span>
          </div>
        <%  }
          } %>
      </div>
    </div>
  </main>

  <footer class="footer container">
    <p class="text-faint">© 2026 ReelBook — Projeto acadêmico.</p>
  </footer>

  <script src="../js/components.js"></script>
  <script src="../js/main.js"></script>
</body>
</html>