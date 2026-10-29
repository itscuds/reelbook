<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>ReelBook — Formulário de filme</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/components.css">

    <style>
        .form-wrap {
            max-width: 820px;
            margin: 36px auto;
            padding: 0 20px;
        }

        .form-panel {
            background: var(--color-bg-elevated);
            border: 1px solid var(--color-border);
            border-radius: 14px;
            padding: 26px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 18px;
        }

        .field {
            display: flex;
            flex-direction: column;
            gap: 7px;
        }

        .field-full {
            grid-column: 1 / -1;
        }

        .field input,
        .field textarea {
            width: 100%;
            background: var(--color-bg);
            border: 1px solid var(--color-border);
            border-radius: 8px;
            padding: 11px;
            color: var(--color-text);
        }

        .field textarea {
            min-height: 130px;
            resize: vertical;
        }

        .genres {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .genres label {
            display: flex;
            gap: 7px;
            align-items: center;
            background: var(--color-bg);
            padding: 8px 12px;
            border-radius: 8px;
        }

        .error {
            background: #4a1e26;
            color: #fecaca;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 16px;
        }

        .buttons {
            display: flex;
            gap: 12px;
            margin-top: 24px;
            flex-wrap: wrap;
        }

        .buttons button,
        .buttons a {
            border: 0;
            cursor: pointer;
            padding: 11px 16px;
            border-radius: 8px;
        }

        .primary {
            background: var(--color-primary);
            color: white;
        }

        .secondary {
            background: var(--color-bg-elevated-2);
        }

        @media (max-width: 600px) {
            .form-grid {
                grid-template-columns: 1fr;
            }

            .field-full {
                grid-column: auto;
            }
        }
    </style>
</head>

<body>
<main class="form-wrap">

    <p class="text-faint">REELBOOK / CATÁLOGO</p>

    <h1 style="margin:8px 0 20px">
        <c:choose>
            <c:when test="${filme.idFilme > 0}">
                Editar filme
            </c:when>
            <c:otherwise>
                Cadastrar filme
            </c:otherwise>
        </c:choose>
    </h1>

    <c:if test="${not empty erro}">
        <div class="error">
            <c:out value="${erro}"/>
        </div>
    </c:if>

    <form class="form-panel"
          method="post"
          action="${pageContext.request.contextPath}/filmes">

        <input type="hidden" name="acao" value="salvar">
        <input type="hidden" name="id" value="${filme.idFilme}">

        <div class="form-grid">

            <div class="field field-full">
                <label for="titulo">Título *</label>

                <input id="titulo"
                       name="titulo"
                       maxlength="200"
                       required
                       value="<c:out value='${filme.titulo}'/>"
                       placeholder="Ex.: Interestelar">
            </div>

            <div class="field">
                <label for="ano">Ano de lançamento *</label>

                <input id="ano"
                       name="ano"
                       type="number"
                       min="1888"
                       max="${anoAtual + 5}"
                       required
                       value="${filme.ano > 0 ? filme.ano : ''}">
            </div>

            <div class="field">
                <label for="duracao">Duração em minutos *</label>

                <input id="duracao"
                       name="duracao"
                       type="number"
                       min="1"
                       max="1000"
                       required
                       value="${filme.duracao > 0 ? filme.duracao : ''}">
            </div>

            <div class="field field-full">
                <label for="posterUrl">URL do pôster</label>

                <input id="posterUrl"
                       name="posterUrl"
                       type="url"
                       maxlength="500"
                       value="<c:out value='${filme.posterUrl}'/>"
                       placeholder="https://...">
            </div>

            <div class="field field-full">
                <label for="sinopse">Sinopse</label>

                <textarea id="sinopse"
                          name="sinopse"
                          maxlength="2000"
                          placeholder="Resumo do filme"><c:out value="${filme.sinopse}"/></textarea>
            </div>

            <div class="field field-full">
                <span>Gêneros</span>

                <div class="genres">
                    <c:forEach var="g" items="${generos}">
                        <label>
                            <input type="checkbox"
                                   name="generos"
                                   value="${g.idGenero}"
                                   <c:forEach var="fg" items="${filme.generos}">
                                       <c:if test="${fg.idGenero == g.idGenero}">
                                           checked
                                       </c:if>
                                   </c:forEach>>

                            <c:out value="${g.nome}"/>
                        </label>
                    </c:forEach>
                </div>

                <small class="text-faint">
                    Os gêneros disponíveis são cadastrados pela equipe
                    responsável por Gêneros.
                </small>
            </div>

        </div>

        <div class="buttons">
            <button class="primary" type="submit">
                Salvar filme
            </button>

            <a class="secondary"
               href="${pageContext.request.contextPath}/filmes">
                Cancelar
            </a>
        </div>

    </form>
</main>
</body>
</html>