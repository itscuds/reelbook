<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>ReelBook — <c:out value="${filme.titulo}"/></title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/components.css">

    <style>
        .detail {
            max-width: 1000px;
            margin: 40px auto;
            padding: 0 24px;
            display: grid;
            grid-template-columns: minmax(200px, 320px) 1fr;
            gap: 32px;
        }

        .poster {
            width: 100%;
            aspect-ratio: 2 / 3;
            object-fit: cover;
            border-radius: 14px;
            background: var(--color-bg-elevated);
        }

        .info {
            background: var(--color-bg-elevated);
            padding: 26px;
            border-radius: 14px;
            border: 1px solid var(--color-border);
        }

        .tag {
            display: inline-block;
            padding: 5px 10px;
            margin: 10px 5px 0 0;
            background: var(--color-bg-elevated-2);
            border-radius: 999px;
        }

        .back {
            display: inline-block;
            margin-bottom: 20px;
            color: var(--color-primary-light);
        }

        @media (max-width: 650px) {
            .detail {
                grid-template-columns: 1fr;
            }

            .poster {
                max-width: 260px;
            }
        }
    </style>
</head>

<body>
<main class="detail">

    <div>
        <a class="back"
           href="${pageContext.request.contextPath}/filmes">
            ← Voltar ao catálogo
        </a>

        <c:choose>
            <c:when test="${not empty filme.posterUrl}">
                <img class="poster"
                     src="<c:out value='${filme.posterUrl}'/>"
                     alt="Pôster de <c:out value='${filme.titulo}'/>"
                     onerror="this.style.display='none'">
            </c:when>

            <c:otherwise>
                <div class="poster"
                     aria-label="Pôster indisponível">
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <section class="info">

        <p class="text-faint">DETALHES DO FILME</p>

        <h1 style="margin:8px 0 12px">
            <c:out value="${filme.titulo}"/>
        </h1>

        <p class="text-muted">
            <c:out value="${filme.ano}"/>
            ·
            <c:out value="${filme.duracao}"/> minutos
        </p>

        <div>
            <c:forEach var="g" items="${filme.generos}">
                <span class="tag">
                    <c:out value="${g.nome}"/>
                </span>
            </c:forEach>
        </div>

        <h2 style="margin-top:28px">Sinopse</h2>

        <p style="margin-top:10px;white-space:pre-line">
            <c:choose>
                <c:when test="${not empty filme.sinopse}">
                    <c:out value="${filme.sinopse}"/>
                </c:when>

                <c:otherwise>
                    Sinopse ainda não cadastrada.
                </c:otherwise>
            </c:choose>
        </p>

        <p style="margin-top:26px">
            <a class="tag"
               href="${pageContext.request.contextPath}/filmes?acao=editar&id=${filme.idFilme}">
                Editar filme
            </a>
        </p>

    </section>
</main>
</body>
</html>