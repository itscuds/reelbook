<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <title>ReelBook — Cadastrar-se</title>

  <link rel="icon" href="../assets/images/logo-icon.png" />
  <link rel="stylesheet" href="../css/style.css" />
  <link rel="stylesheet" href="../css/components.css" />
</head>

<body data-page="cadastro" data-basepath="../">

  <header id="app-navbar" class="navbar container"></header>

  <main
    class="container section"
    style="max-width: 440px; margin-top: var(--space-7);"
  >

    <h1 style="text-align: center; margin-bottom: var(--space-2);">
      Criar Conta
    </h1>

    <p
      class="text-muted"
      style="text-align: center; margin-bottom: var(--space-6);"
    >
      Junte-se ao ReelBook para registrar seus filmes e resenhas.
    </p>

    <% if (request.getAttribute("erro") != null) { %>
      <div
        style="
          background-color: rgba(239, 68, 68, 0.15);
          border: 1px solid var(--color-danger);
          color: var(--color-danger);
          padding: var(--space-3);
          border-radius: var(--radius-md);
          margin-bottom: var(--space-4);
          text-align: center;
        "
      >
        <%= request.getAttribute("erro") %>
      </div>
    <% } %>

    <form
      action="<%= request.getContextPath() %>/cadastro"
      method="post"
      style="display: flex; flex-direction: column; gap: var(--space-4);"
    >

      <div>
        <label
          for="nome"
          class="form-label"
          style="display: block; margin-bottom: var(--space-2);"
        >
          Nome de usuário
        </label>

        <input
          type="text"
          id="nome"
          name="nome"
          required
          class="input"
          style="width: 100%;"
          placeholder="Ex: lilith"
          value="<%= request.getAttribute("nome") == null ? "" : request.getAttribute("nome") %>"
        />
      </div>

      <div>
        <label
          for="email"
          class="form-label"
          style="display: block; margin-bottom: var(--space-2);"
        >
          E-mail
        </label>

        <input
          type="email"
          id="email"
          name="email"
          required
          class="input"
          style="width: 100%;"
          placeholder="seu@email.com"
          value="<%= request.getAttribute("email") == null ? "" : request.getAttribute("email") %>"
        />
      </div>

      <div>
        <label
          for="senha"
          class="form-label"
          style="display: block; margin-bottom: var(--space-2);"
        >
          Senha
        </label>

        <input
          type="password"
          id="senha"
          name="senha"
          required
          class="input"
          style="width: 100%;"
          placeholder="••••••••"
        />
      </div>

      <div>
        <label
          for="confirmaSenha"
          class="form-label"
          style="display: block; margin-bottom: var(--space-2);"
        >
          Confirmar senha
        </label>

        <input
          type="password"
          id="confirmaSenha"
          name="confirmaSenha"
          required
          class="input"
          style="width: 100%;"
          placeholder="••••••••"
        />
      </div>

      <button
        type="submit"
        class="btn btn--primary"
        style="width: 100%; margin-top: var(--space-2);"
      >
        Cadastrar
      </button>

      <p
        class="text-faint"
        style="text-align: center; margin-top: var(--space-3);"
      >
        Já tem uma conta?

        <a
          href="<%= request.getContextPath() %>/pages/login.jsp"
          style="color: var(--color-primary-light);"
        >
          Entrar
        </a>
      </p>

    </form>

  </main>

  <footer
    class="footer container"
    style="margin-top: var(--space-8);"
  >
    <p class="text-faint">
      © 2026 ReelBook — Projeto acadêmico.
    </p>
  </footer>

  <script src="../js/components.js"></script>
  <script src="../js/main.js"></script>

</body>
</html>