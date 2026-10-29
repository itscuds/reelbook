<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>ReelBook — Catálogo de filmes</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/components.css">

    <style>
        .admin-wrap {
            max-width: 1200px;
            margin: auto;
            padding: 36px 24px;
        }

        .admin-head {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            margin-bottom: 24px;
            flex-wrap: wrap;
        }

        .admin-panel {
            background: var(--color-bg-elevated);
            border: 1px solid var(--color-border);
            border-radius: 14px;
            padding: 20px;
            margin-bottom: 24px;
        }

        .filters {
            display: grid;
            grid-template-columns: 2fr 1fr 1fr auto;
            gap: 12px;
        }

        .filters input,
        .filters select {
            width: 100%;
            background: var(--color-bg);
            border: 1px solid var(--color-border);
            border-radius: 8px;
            padding: 11px;
            color: var(--color-text);
        }

        .btn-link {
            display: inline-block;
            padding: 10px 15px;
            border-radius: 8px;
            background: var(--color-primary);
            color: white;
            font-weight: 600;
        }

        .table-wrap {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            text-align: left;
            padding: 13px 10px;
            border-bottom: 1px solid var(--color-border);
            vertical-align: top;
        }

        th {
            color: var(--color-text-muted);
            font-size: .85rem;
        }

        td.actions {
            white-space: nowrap;
        }

        td a {
            color: var(--color-primary-light);
            margin-right: 10px;
        }

        .pill {
            display: inline-block;
            padding: 3px 8px;
            border-radius: 999px;
            background: var(--color-bg-elevated-2);
            font-size: .8rem;
        }

        .notice {
            padding: 12px 16px;
            background: #153d2a;
            border-radius: 8px;
            margin-bottom: 18px;
        }

        .empty {
            padding: 32px;
            text-align: center;
            color: var(--color-text-muted);
        }

        @media (max-width: 700px) {
            .filters {
                grid-template-columns: 1fr;
            }

            .admin-wrap {
                padding: 24px 16px;
            }
        }
    </style>
</head>

<body>
<main class="admin-wrap">

    <div class="admin-head">
        <div>
            <p class="text-faint">REELBOOK / ADMINISTRAÇÃO</p>
            <h1>Catálogo de filmes</h1>
            <p class="text-muted">
                Cadastre, pesquise e mantenha os filmes do catálogo.
            </p>
        </div>

        <a class="btn-link"
           href="${pageContext.request.contextPath}/filmes?acao=novo">
            + Novo filme
        </a>
    </div>

    <c:if test="${param.mensagem == 'salvo'}">
        <div class="notice">Filme salvo com sucesso.</div>
    </c:if>

    <c:if test="${param.mensagem == 'desativado'}">
        <div class="notice">
            Filme desativado. Ele não aparece no catálogo público.
        </div>
    </c:if>

    <section class="admin-panel">
        <form method="get"
              action="${pageContext.request.contextPath}/filmes"
              class="filters">

            <input type="search"
                   name="busca"
                   placeholder="Buscar pelo título..."
                   value="<c:out value='${busca}'/>"
                   aria-label="Buscar título">

            <select name="genero">
                <option value="">Todos os gêneros</option>

                <c:forEach var="g" items="${generos}">
                    <option value="${g.idGenero}"
                        <c:if test="${generoSelecionado == g.idGenero}">
                            selected
                        </c:if>>
                        <c:out value="${g.nome}"/>
                    </option>
                </c:forEach>
            </select>

            <select name="ano">
                <option value="">Todos os anos</option>

                <c:forEach var="a" items="${anos}">
                    <option value="${a}"
                        <c:if test="${anoSelecionado == a}">
                            selected
                        </c:if>>
                        ${a}
                    </option>
                </c:forEach>
            </select>

            <button class="btn-link" type="submit">
                Filtrar
            </button>
        </form>

        <p style="margin-top:12px">
            <a href="${pageContext.request.contextPath}/filmes"
               class="text-muted">
                Limpar filtros
            </a>
        </p>
    </section>

    <section class="admin-panel table-wrap">

        <c:choose>
            <c:when test="${empty filmes}">
                <div class="empty">
                    Nenhum filme encontrado.
                    Tente mudar os filtros ou cadastre um novo filme.
                </div>
            </c:when>

            <c:otherwise>
                <table>
                    <thead>
                    <tr>
                        <th>Filme</th>
                        <th>Ano</th>
                        <th>Duração</th>
                        <th>Gêneros</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="f" items="${filmes}">
                        <tr>
                            <td>
                                <strong>
                                    <c:out value="${f.titulo}"/>
                                </strong>

                                <c:if test="${not empty f.sinopse}">
                                    <div class="text-faint">
                                        <c:out value="${f.sinopse}"/>
                                    </div>
                                </c:if>
                            </td>

                            <td>${f.ano}</td>
                            <td>${f.duracao} min</td>

                            <td>
                                <c:forEach var="g" items="${f.generos}">
                                    <span class="pill">
                                        <c:out value="${g.nome}"/>
                                    </span>
                                </c:forEach>

                                <c:if test="${empty f.generos}">
                                    —
                                </c:if>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${f.ativo}">
                                        Ativo
                                    </c:when>
                                    <c:otherwise>
                                        Inativo
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/filmes?acao=detalhe&id=${f.idFilme}">
                                    Detalhes
                                </a>

                                <a href="${pageContext.request.contextPath}/filmes?acao=editar&id=${f.idFilme}">
                                    Editar
                                </a>

                                <c:if test="${f.ativo}">
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/filmes"
                                          style="display:inline"
                                          onsubmit="return confirm('Deseja desativar este filme?')">

                                        <input type="hidden"
                                               name="acao"
                                               value="excluir">

                                        <input type="hidden"
                                               name="id"
                                               value="${f.idFilme}">

                                        <button type="submit"
                                                style="border:0;background:none;color:#f87171;cursor:pointer">
                                            Desativar
                                        </button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>

    </section>

    <p>
        <a class="text-muted"
           href="${pageContext.request.contextPath}/">
            ← Voltar à página inicial
        </a>
    </p>

</main>
</body>
</html>