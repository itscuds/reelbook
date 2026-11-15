<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>ReelBook — Gêneros</title>

  <link rel="icon" href="${pageContext.request.contextPath}/assets/images/logo-icon.png" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css" />

  <style>
    .panel { background: var(--color-bg-elevated); border: 1px solid var(--color-border);
             border-radius: 14px; padding: 22px; margin-bottom: 24px; }
    .genero-form { display: grid; grid-template-columns: 2fr 3fr 90px auto; gap: 10px;
                   align-items: center; }
    .genero-form input { background: var(--color-bg); border: 1px solid var(--color-border);
                         border-radius: 8px; padding: 10px; color: var(--color-text); width: 100%; }
    .genero-row { display: flex; gap: 10px; align-items: center; margin-bottom: 10px; flex-wrap: wrap; }
    .genero-row.is-inativo { opacity: .55; }
    .genero-row form { margin: 0; }
    .aviso { padding: 12px; border-radius: 8px; margin-bottom: 16px; }
    .aviso--ok { background: #17382a; color: #bbf7d0; }
    .aviso--erro { background: #4a1e26; color: #fecaca; }
    @media (max-width: 700px) { .genero-form { grid-template-columns: 1fr; } }
  </style>
</head>

<body data-page="generos" data-basepath="${pageContext.request.contextPath}/">

  <header id="app-navbar" class="navbar container"></header>

  <main class="container section">
    <div class="section__header">
      <h1>Gêneros</h1>
    </div>

    <c:if test="${not empty mensagem}">
      <div class="aviso aviso--ok"><c:out value="${mensagem}"/></div>
    </c:if>
    <c:if test="${not empty erro}">
      <div class="aviso aviso--erro"><c:out value="${erro}"/></div>
    </c:if>

    <!-- CADASTRAR -->
    <section class="panel">
      <h2>Novo gênero</h2>
      <form class="genero-form" method="post"
            action="${pageContext.request.contextPath}/generos">
        <input type="hidden" name="acao" value="cadastrar" />
        <input name="nome" placeholder="Nome" maxlength="60" required />
        <input name="descricao" placeholder="Descrição (opcional)" maxlength="255" />
        <input name="ordem" type="number" min="1" placeholder="Ordem" />
        <button class="btn btn--primary btn--sm" type="submit">Cadastrar</button>
      </form>
    </section>

    <!-- LISTAR / RENOMEAR / DESATIVAR -->
    <section class="panel">
      <h2>Gêneros cadastrados</h2>

      <c:if test="${empty generos}">
        <p class="text-faint">Nenhum gênero cadastrado.</p>
      </c:if>

      <c:forEach var="g" items="${generos}">
        <div class="genero-row ${g.ativo ? '' : 'is-inativo'}">

          <form class="genero-form" method="post"
                action="${pageContext.request.contextPath}/generos" style="flex:1">
            <input type="hidden" name="acao" value="renomear" />
            <input type="hidden" name="id" value="${g.idGenero}" />
            <input name="nome" value="<c:out value='${g.nome}'/>" maxlength="60" required />
            <input name="descricao" value="<c:out value='${g.descricao}'/>" maxlength="255" />
            <input name="ordem" type="number" min="1" value="${g.ordem}" />
            <button class="btn btn--outline btn--sm" type="submit">Salvar</button>
          </form>

          <form method="post" action="${pageContext.request.contextPath}/generos">
            <input type="hidden" name="id" value="${g.idGenero}" />
            <c:choose>
              <c:when test="${g.ativo}">
                <input type="hidden" name="acao" value="desativar" />
                <button class="btn btn--outline btn--sm" type="submit"
                        onclick="return confirm('Desativar este gênero?');">Desativar</button>
              </c:when>
              <c:otherwise>
                <input type="hidden" name="acao" value="reativar" />
                <button class="btn btn--primary btn--sm" type="submit">Reativar</button>
              </c:otherwise>
            </c:choose>
          </form>
        </div>
      </c:forEach>
    </section>
  </main>

  <footer class="footer container">
    <p class="text-faint">© 2026 ReelBook — Projeto acadêmico.</p>
  </footer>

  <script src="${pageContext.request.contextPath}/js/components.js"></script>
  <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
