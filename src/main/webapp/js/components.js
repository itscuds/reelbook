
// =============================================================
// components.js
// Componentes compartilhados do ReelBook
// =============================================================

// Links da navbar
const NAV_LINKS = [
  { id: 'home', label: 'Início', href: 'index.jsp' },
  { id: 'explorar', label: 'Explorar', href: 'pages/explorar.jsp' },
  { id: 'listas', label: 'Listas', href: 'pages/listas.jsp' },
  { id: 'diario', label: 'Diário', href: 'minha-lista?tipo=diario' }
];

// Escapa caracteres para evitar inserir texto do usuário como HTML
function escaparHTML(valor) {
  return String(valor == null ? '' : valor).replace(/[&<>"']/g, caractere => {
    const caracteres = {
      '&': '&amp;',
      '<': '&lt;',
      '>': '&gt;',
      '"': '&quot;',
      "'": '&#39;'
    };

    return caracteres[caractere];
  });
}

// Monta a navbar
function renderNavbar({
                        basePath = '',
                        activePage = '',
                        usuario = null
                      } = {}) {

  const container = document.getElementById('app-navbar');

  if (!container) return;

  const linksHTML = NAV_LINKS.map(link => `
    <a href="${basePath}${link.href}"
       class="${link.id === activePage ? 'is-active' : ''}">
      ${link.label}
    </a>
  `).join('');

  let actionsHTML = '';

  if (usuario) {

    const nome = escaparHTML(usuario.nome || 'Usuário');

    const iniciais = nome
        .trim()
        .split(/\s+/)
        .slice(0, 2)
        .map(parte => parte.charAt(0))
        .join('')
        .toUpperCase();

    actionsHTML = `
      <button class="icon-btn"
              type="button"
              aria-label="Buscar">
        ${ICON_SEARCH}
      </button>

      <a href="${basePath}perfil"
         title="Meu perfil"
         aria-label="Meu perfil">

        <span class="avatar"
              style="display: flex; align-items: center; justify-content: center; font-weight: 700;">
          ${iniciais}
        </span>

      </a>

      <a href="${basePath}logout"
         class="btn btn--outline btn--sm">
        Sair
      </a>
    `;

  } else {

    actionsHTML = `
      <a href="${basePath}pages/login.jsp"
         class="btn btn--outline btn--sm">
        Entrar
      </a>

      <a href="${basePath}pages/cadastro.jsp"
         class="btn btn--primary btn--sm">
        Criar conta
      </a>
    `;
  }

  container.innerHTML = `
    <a href="${basePath}index.jsp" class="logo">

      <img src="${basePath}assets/images/logo-icon.png"
           alt="ReelBook" />

      <span class="logo__text">
        <span class="logo__reel">Reel</span><span class="logo__book">Book</span>
      </span>

    </a>

    <nav class="navbar__links">
      ${linksHTML}
    </nav>

    <div class="navbar__actions">
      ${actionsHTML}
    </div>
  `;
}

// Ícone de busca
const ICON_SEARCH = `
  <svg width="20"
       height="20"
       viewBox="0 0 24 24"
       fill="none"
       stroke="currentColor"
       stroke-width="2">

    <circle cx="11" cy="11" r="8" />

    <line x1="21"
          y1="21"
          x2="16.65"
          y2="16.65" />

  </svg>
`;

// =============================================================
// Cards de filmes
// =============================================================

function criarMovieCardHTML(filme, basePath = '') {

  return `
    <a class="movie-card"
       href="${basePath}pages/filme.jsp?id=${filme.id}">

      <img class="movie-card__poster"
           src="${filme.posterUrl}"
           alt="Pôster de ${filme.title}"
           loading="lazy" />

      <div class="movie-card__info">

        <p class="movie-card__title">
          ${filme.title}
        </p>

        <div class="movie-card__meta">

          <span>${filme.year}</span>

          <span class="movie-card__rating">
            ★ ${formatarNota(filme.rating)}
          </span>

        </div>

      </div>

    </a>
  `;
}

// Renderiza uma lista de filmes
function renderMovieGrid(container, filmes, basePath = '') {

  if (!container) return;

  if (!filmes || filmes.length === 0) {

    container.outerHTML = `
      <div class="empty-state">
        Nenhum filme para mostrar ainda.
      </div>
    `;

    return;
  }

  container.innerHTML = filmes
      .map(filme => criarMovieCardHTML(filme, basePath))
      .join('');
}
