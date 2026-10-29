<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="br.com.reelbook.model.Usuario" %>
<%@ page import="br.com.reelbook.dao.UsuarioDAO.PerfilStatsDTO" %>
<%@ page import="br.com.reelbook.dao.DiarioDAO.DiarioDTO" %>
<%@ page import="br.com.reelbook.dao.ResenhaDAO.ResenhaDTO" %>
<%@ page import="java.util.List" %>
<%
  Usuario usuario = (Usuario) request.getAttribute("usuario");
  if (usuario == null) {
    response.sendRedirect(request.getContextPath() + "/perfil");
    return;
  }
  PerfilStatsDTO stats = (PerfilStatsDTO) request.getAttribute("stats");
  List<DiarioDTO> diario = (List<DiarioDTO>) request.getAttribute("diario");
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
  <style>
    .tab-content { display: none; margin-top: var(--space-5); }
    .tab-content.is-active { display: block; }
  </style>
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
      <div><strong><%= stats != null ? stats.totalListas : 0 %></strong><span class="text-faint">Listas</span></div>
      <div><strong><%= stats != null ? stats.totalAssistidos : 0 %></strong><span class="text-faint">Filmes assistidos</span></div>
      <div><strong><%= stats != null ? stats.totalResenhas : 0 %></strong><span class="text-faint">Avaliações</span></div>
      <div><strong><%= stats != null ? stats.totalFavoritos : 0 %></strong><span class="text-faint">Favoritos</span></div>
    </div>

    <nav class="perfil-tabs">
      <a href="javascript:void(0)" class="tab-link is-active" onclick="trocarAba(event, 'aba-atividade')">Atividade</a>
      <a href="javascript:void(0)" class="tab-link" onclick="trocarAba(event, 'aba-avaliacoes')">Avaliações</a>
      <a href="javascript:void(0)" class="tab-link" onclick="trocarAba(event, 'aba-listas')">Listas</a>
      <a href="javascript:void(0)" class="tab-link" onclick="trocarAba(event, 'aba-diario')">Diário</a>
    </nav>

    <div id="aba-atividade" class="tab-content is-active">
      <p class="text-muted">Últimos filmes registrados no diário:</p>
      <div class="diario-lista" style="margin-top: var(--space-4);">
        <% if (diario == null || diario.isEmpty()) { %>
          <div class="empty-state">Nenhuma atividade recente registrada.</div>
        <% } else { 
            for (int i = 0; i < Math.min(diario.size(), 3); i++) { 
              DiarioDTO d = diario.get(i);
        %>
          <div class="diario-item">
            <img class="diario-item__poster" src="<%= d.posterUrl != null ? d.posterUrl : "../assets/images/logo-icon.png" %>" alt="<%= d.titulo %>" />
            <div class="diario-item__info">
              <p class="diario-item__titulo"><%= d.titulo %> <span class="text-faint">(<%= d.ano %>)</span></p>
              <p class="movie-card__rating">★ <%= d.nota != null ? String.format("%.1f", d.nota) : "-" %></p>
            </div>
            <span class="text-faint"><%= d.dataRegistro %></span>
          </div>
        <%  }
          } %>
      </div>
    </div>

    <div id="aba-avaliacoes" class="tab-content">
      <div class="diario-lista">
        <% if (resenhas == null || resenhas.isEmpty()) { %>
          <div class="empty-state">Você ainda não escreveu nenhuma avaliação ou resenha.</div>
        <% } else { 
            for (ResenhaDTO r : resenhas) {
        %>
          <div class="diario-item">
            <img class="diario-item__poster" src="<%= r.posterUrl != null ? r.posterUrl : "../assets/images/logo-icon.png" %>" alt="<%= r.tituloFilme %>" />
            <div class="diario-item__info">
              <p class="diario-item__titulo"><%= r.tituloFilme %> <span class="text-faint">(<%= r.anoFilme %>)</span></p>
              <p class="movie-card__rating">★ <%= r.nota != null ? String.format("%.1f", r.nota) : "-" %></p>
              <p class="text-muted" style="margin-top: 6px;"><%= r.texto %></p>
            </div>
            <span class="text-faint"><%= r.dataPublicacao %></span>
          </div>
        <%  }
          } %>
      </div>
    </div>

    <div id="aba-listas" class="tab-content">
      <div class="empty-state">Nenhuma lista personalizada criada até o momento.</div>
    </div>

    <div id="aba-diario" class="tab-content">
      <div class="diario-lista">
        <% if (diario == null || diario.isEmpty()) { %>
          <div class="empty-state">Seu diário está vazio.</div>
        <% } else { 
            for (DiarioDTO d : diario) {
        %>
          <div class="diario-item">
            <img class="diario-item__poster" src="<%= d.posterUrl != null ? d.posterUrl : "../assets/images/logo-icon.png" %>" alt="<%= d.titulo %>" />
            <div class="diario-item__info">
              <p class="diario-item__titulo"><%= d.titulo %> <span class="text-faint">(<%= d.ano %>)</span></p>
              <p class="movie-card__rating">★ <%= d.nota != null ? String.format("%.1f", d.nota) : "-" %></p>
              <p class="text-muted"><%= d.comentario != null ? d.comentario : "" %></p>
            </div>
            <span class="text-faint"><%= d.dataRegistro %></span>
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
  <script>
    function trocarAba(evt, idAba) {
      document.querySelectorAll('.tab-link').forEach(btn => btn.classList.remove('is-active'));
      document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('is-active'));
      evt.currentTarget.classList.add('is-active');
      document.getElementById(idAba).classList.add('is-active');
    }
  </script>
</body>
</html>