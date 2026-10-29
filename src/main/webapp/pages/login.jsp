<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>ReelBook — Entrar</title>
  <link rel="icon" href="../assets/images/logo-icon.png" />
  <link rel="stylesheet" href="../css/style.css" />
  <link rel="stylesheet" href="../css/components.css" />
</head>
<body data-page="login" data-basepath="../">

  <header id="app-navbar" class="navbar container"></header>

  <main class="container section" style="max-width: 440px; margin-top: var(--space-8);">
    <h1 style="text-align: center; margin-bottom: var(--space-2);">Entrar no ReelBook</h1>
    <p class="text-muted" style="text-align: center; margin-bottom: var(--space-6);">
      Inicie sessão para gerir o seu diário e resenhas.
    </p>

    <% if (request.getAttribute("erro") != null) { %>
      <div style="background-color: rgba(239, 68, 68, 0.15); border: 1px solid var(--color-danger); color: var(--color-danger); padding: var(--space-3); border-radius: var(--radius-md); margin-bottom: var(--space-4); text-align: center;">
        <%= request.getAttribute("erro") %>
      </div>
    <% } %>

    <form action="<%= request.getContextPath() %>/login" method="post" style="display: flex; flex-direction: column; gap: var(--space-4);">
      <div>
        <label for="email" class="form-label" style="display: block; margin-bottom: var(--space-2);">E-mail</label>
        <input type="email" id="email" name="email" required class="input" style="width: 100%;" placeholder="seu@email.com" />
      </div>

      <div>
        <label for="senha" class="form-label" style="display: block; margin-bottom: var(--space-2);">Palavra-passe</label>
        <input type="password" id="senha" name="senha" required class="input" style="width: 100%;" placeholder="••••••••" />
      </div>

      <button type="submit" class="btn btn--primary" style="width: 100%; margin-top: var(--space-2);">Entrar</button>

      <p class="text-faint" style="text-align: center; margin-top: var(--space-3);">
        Ainda não tem conta? <a href="<%= request.getContextPath() %>/pages/cadastro.jsp" style="color: var(--color-primary-light);">Criar conta</a>
      </p>
    </form>
  </main>

  <footer class="footer container" style="margin-top: var(--space-8);">
    <p class="text-faint">© 2026 ReelBook — Projeto acadêmico.</p>
  </footer>

  <script src="../js/components.js"></script>
  <script src="../js/main.js"></script>
</body>
</html>