<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="br.com.reelbook.dao.DiarioDAO.DiarioDTO" %>
<%@ page import="java.util.List" %>
<%
  List<DiarioDTO> registros = (List<DiarioDTO>) request.getAttribute("registros");
  if (registros == null && request.getParameter("load") == null) {
    response.sendRedirect(request.getContextPath() + "/diario");
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
  <style>
    .modal-overlay {
      position: fixed; inset: 0; background: rgba(0,0,0,0.75);
      display: none; justify-content: center; align-items: center; z-index: 100;
    }
    .modal-overlay.open { display: flex; }
    .modal-card {
      background: var(--color-bg-elevated); padding: var(--space-6);
      border-radius: var(--radius-lg); width: 100%; max-width: 440px;
      border: 1px solid var(--color-border);
    }
  </style>
</head>
<body data-page="diario" data-basepath="../">

  <header id="app-navbar" class="navbar container"></header>

  <main id="main-conteudo" class="container section">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-5);">
      <h1>Diário</h1>
      <button class="btn btn--primary" onclick="abrirModal()">+ Marcar Filme</button>
    </div>

    <div id="diario-lista" class="diario-lista">
      <% if (registros == null || registros.isEmpty()) { %>
        <div class="empty-state">Nenhum filme registrado como assistido no seu diário.</div>
      <% } else {
          for (DiarioDTO r : registros) {
      %>
        <div class="diario-item">
          <img class="diario-item__poster"
               src="<%= (r.posterUrl != null && !r.posterUrl.isEmpty()) ? r.posterUrl : "../assets/images/logo-icon.png" %>"
               alt="Pôster de <%= r.titulo %>" />
          <div class="diario-item__info">
            <p class="diario-item__titulo"><%= r.titulo %> <span class="text-faint">(<%= r.ano %>)</span></p>
            <p class="movie-card__rating">★ <%= (r.nota != null) ? String.format("%.1f", r.nota) : "Sem nota" %></p>
            <p class="text-muted"><%= (r.comentario != null) ? r.comentario : "" %></p>

            <form action="<%= request.getContextPath() %>/diario" method="post" style="margin-top: var(--space-2);">
              <input type="hidden" name="acao" value="excluir" />
              <input type="hidden" name="idFilme" value="<%= r.idFilme %>" />
              <button type="submit" class="btn btn--outline btn--sm" style="color: var(--color-danger); border-color: var(--color-danger);" onclick="return confirm('Deseja remover este registro?')">Excluir</button>
            </form>
          </div>
          <span class="text-faint"><%= r.dataRegistro %></span>
        </div>
      <%  }
        } %>
    </div>
  </main>

  <div id="modal-assistido" class="modal-overlay">
    <div class="modal-card">
      <h2 style="font-size: var(--fs-lg); margin-bottom: var(--space-4);">Marcar como Assistido</h2>
      <form action="<%= request.getContextPath() %>/diario" method="post">
        <input type="hidden" name="acao" value="salvar" />

        <label class="form-label" style="display:block; margin-bottom: var(--space-2);">ID do Filme (Cadastrado no Banco):</label>
        <input type="number" name="idFilme" required class="input" style="width: 100%; margin-bottom: var(--space-4);" placeholder="Ex: 1" />

        <label class="form-label" style="display:block; margin-bottom: var(--space-2);">Nota (0.0 a 10.0):</label>
        <input type="number" step="0.1" min="0" max="10" name="nota" class="input" style="width: 100%; margin-bottom: var(--space-4);" placeholder="Ex: 8.5" />

        <label class="form-label" style="display:block; margin-bottom: var(--space-2);">Comentário / Resenha:</label>
        <textarea name="comentario" rows="3" class="input" style="width: 100%; margin-bottom: var(--space-5);" placeholder="O que você achou do filme?"></textarea>

        <div style="display: flex; gap: var(--space-3); justify-content: flex-end;">
          <button type="button" class="btn btn--outline" onclick="fecharModal()">Cancelar</button>
          <button type="submit" class="btn btn--primary">Salvar no Diário</button>
        </div>
      </form>
    </div>
  </div>

  <footer class="footer container">
    <p class="text-faint">© 2026 ReelBook — Projeto acadêmico.</p>
  </footer>

  <script src="../js/components.js"></script>
  <script src="../js/main.js"></script>
  <script>
    function abrirModal() { document.getElementById('modal-assistido').classList.add('open'); }
    function fecharModal() { document.getElementById('modal-assistido').classList.remove('open'); }
  </script>
</body>
</html>