<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>ReelBook — <c:out value="${filme.titulo}"/></title>

    <link
        rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css"
    >

    <link
        rel="stylesheet"
        href="${pageContext.request.contextPath}/css/components.css"
    >

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

        .acoes {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            margin-top: 24px;
        }

        .acoes form {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            gap: 8px;
        }

        .acoes button {
            cursor: pointer;
            padding: 8px 16px;
            background: var(--color-primary);
            color: #fff;
            border: 1px solid transparent;
            border-radius: var(--radius-md);
            font-weight: 600;
        }

        .acoes button:hover {
            background: var(--color-primary-hover);
        }

        .acoes input,
        .acoes select {
            max-width: 140px;
            padding: 8px;
            background: var(--color-bg-elevated-2);
            color: var(--color-text);
            border: 1px solid var(--color-border);
            border-radius: var(--radius-md);
        }

        .resenhas {
            grid-column: 1 / -1;
            background: var(--color-bg-elevated);
            padding: 26px;
            border-radius: 14px;
            border: 1px solid var(--color-border);
        }

        .resenhas textarea {
            width: 100%;
            padding: 10px;
            background: var(--color-bg-elevated-2);
            color: var(--color-text);
            border: 1px solid var(--color-border);
            border-radius: var(--radius-md);
            font-family: inherit;
            resize: vertical;
        }

        .resenhas button {
            cursor: pointer;
            padding: 8px 16px;
            background: var(--color-primary);
            color: #fff;
            border: 1px solid transparent;
            border-radius: var(--radius-md);
            font-weight: 600;
        }

        .resenhas button:hover {
            background: var(--color-primary-hover);
        }

        .resenha-form {
            margin: 16px 0 24px;
        }

        .resenha-rodape {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 8px;
        }

        .resenha-erro {
            margin-top: 12px;
            padding: 10px 14px;
            border: 1px solid #dc3545;
            border-radius: var(--radius-md);
            color: #f8d7da;
        }

        .resenha {
            padding: 16px 0;
            border-top: 1px solid var(--color-border);
        }

        .resenha-texto {
            margin-top: 8px;
            white-space: pre-line;
        }

        .resenha-acoes {
            display: flex;
            flex-wrap: wrap;
            align-items: flex-start;
            gap: 12px;
            margin-top: 12px;
        }

        .resenha-acoes summary {
            cursor: pointer;
            color: var(--color-primary-light);
        }

        .resenha-acoes details form {
            margin-top: 8px;
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

<body
    data-page="detalhe"
    data-basepath="${pageContext.request.contextPath}/"
>

<header id="app-navbar" class="navbar container"></header>

<main class="detail">

    <div>

        <a
            class="back"
            href="${pageContext.request.contextPath}/filmes"
        >
            ← Voltar ao catálogo
        </a>

        <c:choose>
            <c:when test="${not empty filme.posterUrl}">
                <img
                    class="poster"
                    src="<c:out value='${filme.posterUrl}'/>"
                    alt="Pôster de <c:out value='${filme.titulo}'/>"
                >
            </c:when>

            <c:otherwise>
                <div
                    class="poster"
                    aria-label="Pôster indisponível"
                ></div>
            </c:otherwise>
        </c:choose>

    </div>

    <section class="info">

        <p class="text-faint">
            DETALHES DO FILME
        </p>

        <h1 style="margin: 8px 0 12px">
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

        <h2 style="margin-top: 28px">
            Sinopse
        </h2>

        <p style="margin-top: 10px; white-space: pre-line">
            <c:choose>
                <c:when test="${not empty filme.sinopse}">
                    <c:out value="${filme.sinopse}"/>
                </c:when>

                <c:otherwise>
                    Sinopse ainda não cadastrada.
                </c:otherwise>
            </c:choose>
        </p>

        <h2 style="margin-top: 28px">
            Minha lista pessoal
        </h2>

        <div class="acoes">

            <form
                action="${pageContext.request.contextPath}/registro-pessoal"
                method="post"
            >
                <input
                    type="hidden"
                    name="acao"
                    value="adicionar"
                >

                <input
                    type="hidden"
                    name="idFilme"
                    value="<c:out value='${filme.idFilme}'/>"
                >

                <button type="submit">
                    + Adicionar à minha lista
                </button>
            </form>

            <form
                action="${pageContext.request.contextPath}/registro-pessoal"
                method="post"
            >
                <input
                    type="hidden"
                    name="acao"
                    value="atualizar"
                >

                <input
                    type="hidden"
                    name="idFilme"
                    value="<c:out value='${filme.idFilme}'/>"
                >

                <input
                    type="hidden"
                    name="status"
                    value="ASSISTIDO"
                >

                <button type="submit">
                    Marcar como assistido
                </button>
            </form>

            <c:choose>
                <c:when test="${ehFavorito}">
                    <form
                        action="${pageContext.request.contextPath}/registro-pessoal"
                        method="post"
                    >
                        <input type="hidden" name="acao" value="atualizar">
                        <input type="hidden" name="idFilme" value="<c:out value='${filme.idFilme}'/>">
                        <input type="hidden" name="favorito" value="false">

                        <button type="submit">
                            Remover dos favoritos
                        </button>
                    </form>
                </c:when>

                <c:otherwise>
            <form
                action="${pageContext.request.contextPath}/registro-pessoal"
                method="post"
            >
                <input
                    type="hidden"
                    name="acao"
                    value="adicionar"
                >

                <input
                    type="hidden"
                    name="idFilme"
                    value="<c:out value='${filme.idFilme}'/>"
                >

                <input
                    type="hidden"
                    name="favorito"
                    value="true"
                >

                <button type="submit">
                    Adicionar aos favoritos
                </button>
            </form>
                </c:otherwise>
            </c:choose>

            <form
                action="${pageContext.request.contextPath}/registro-pessoal"
                method="post"
            >
                <input
                    type="hidden"
                    name="acao"
                    value="atualizar"
                >

                <input
                    type="hidden"
                    name="idFilme"
                    value="<c:out value='${filme.idFilme}'/>"
                >
                 <input
                    type="hidden"
                    name="status"
                    value="ASSISTIDO"
                >
                <label for="nota">
                    Minha nota (0 a 5)
                </label>

                <input
                    class="input"
                    id="nota"
                    type="number"
                    name="nota"
                    min="0"
                    max="5"
                    step="0.5"
                    required
                >

                <button type="submit">
                    Salvar nota
                </button>
            </form>

        </div>

  <%-- Só o curador vê o botão de editar --%>
        <c:if test="${sessionScope.usuarioLogado.perfil == 'CURADOR'}">
        <p style="margin-top: 20px">
            <a
                class="tag"
                href="${pageContext.request.contextPath}/filmes?acao=editar&id=${filme.idFilme}"
            >
                Editar filme
            </a>
        </p>
    </c:if>

    </section>

    <section class="resenhas" id="resenhas">

        <h2>Resenhas</h2>

        <c:if test="${param.erroResenha == 'vazia'}">
            <p class="resenha-erro">Escreva o texto da resenha antes de publicar.</p>
        </c:if>

        <c:if test="${param.erroResenha == 'longa'}">
            <p class="resenha-erro">A resenha deve ter no máximo 2000 caracteres.</p>
        </c:if>

        <c:choose>
            <c:when test="${not empty sessionScope.usuarioLogado}">
                <form
                    class="resenha-form"
                    action="${pageContext.request.contextPath}/resenhas"
                    method="post"
                >
                    <input type="hidden" name="acao" value="salvar">

                    <input
                        type="hidden"
                        name="idFilme"
                        value="<c:out value='${filme.idFilme}'/>"
                    >

                    <label for="texto-resenha">Escreva sua resenha</label>

                    <textarea
                        id="texto-resenha"
                        name="texto"
                        rows="4"
                        maxlength="2000"
                        required
                    ></textarea>

                    <div class="resenha-rodape">
                        <span id="contador-resenha" class="text-faint">0 / 2000</span>
                        <button type="submit">Publicar resenha</button>
                    </div>
                </form>
            </c:when>

            <c:otherwise>
                <p class="text-muted" style="margin: 16px 0 24px">
                    <a href="${pageContext.request.contextPath}/pages/login.jsp">Entre</a>
                    para escrever uma resenha.
                </p>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${empty resenhas}">
                <p class="text-muted">Este filme ainda não tem resenhas.</p>
            </c:when>

            <c:otherwise>
                <c:forEach var="r" items="${resenhas}">
                    <article class="resenha">

                        <header>
                            <strong><c:out value="${r.nomeUsuario}"/></strong>
                            <span class="text-faint">
                                · <c:out value="${r.dataPublicacao}"/>
                            </span>
                        </header>

                        <p class="resenha-texto"><c:out value="${r.texto}"/></p>

                        <c:if test="${not empty sessionScope.usuarioLogado and r.idUsuario == sessionScope.usuarioLogado.id}">
                            <div class="resenha-acoes">

                                <details>
                                    <summary>Editar</summary>

                                    <form
                                        action="${pageContext.request.contextPath}/resenhas"
                                        method="post"
                                    >
                                        <input type="hidden" name="acao" value="editar">
                                        <input type="hidden" name="origem" value="detalhe">

                                        <input
                                            type="hidden"
                                            name="idResenha"
                                            value="<c:out value='${r.idResenha}'/>"
                                        >

                                        <input
                                            type="hidden"
                                            name="idFilme"
                                            value="<c:out value='${filme.idFilme}'/>"
                                        >

                                        <textarea
                                            name="texto"
                                            rows="4"
                                            maxlength="2000"
                                            required
                                        ><c:out value="${r.texto}"/></textarea>

                                        <div class="resenha-rodape">
                                            <span></span>
                                            <button type="submit">Salvar alteração</button>
                                        </div>
                                    </form>
                                </details>

                                <form
                                    action="${pageContext.request.contextPath}/resenhas"
                                    method="post"
                                    onsubmit="return confirm('Apagar esta resenha?');"
                                >
                                    <input type="hidden" name="acao" value="excluir">
                                    <input type="hidden" name="origem" value="detalhe">

                                    <input
                                        type="hidden"
                                        name="idResenha"
                                        value="<c:out value='${r.idResenha}'/>"
                                    >

                                    <input
                                        type="hidden"
                                        name="idFilme"
                                        value="<c:out value='${filme.idFilme}'/>"
                                    >

                                    <button type="submit">Apagar</button>
                                </form>

                            </div>
                        </c:if>

                    </article>
                </c:forEach>
            </c:otherwise>
        </c:choose>

    </section>

</main>

<script src="${pageContext.request.contextPath}/js/components.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>

<script>
    (function () {
        var campo = document.getElementById('texto-resenha');
        var contador = document.getElementById('contador-resenha');

        if (campo && contador) {
            campo.addEventListener('input', function () {
                contador.textContent = campo.value.length + ' / 2000';
            });
        }
    })();
</script>

</body>
</html>