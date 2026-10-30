// Componentes compartilhados do ReelBook.

const NAV_LINKS = [
  { id: 'home', label: 'Início', href: 'index.jsp' },
  { id: 'explorar', label: 'Explorar', href: 'filmes?acao=explorar' },
  { id: 'listas', label: 'Listas', href: 'pages/listas.jsp' },
  { id: 'diario', label: 'Diário', href: 'minha-lista?tipo=diario' }
];

function escaparHTML(valor) {
  return String(valor == null ? '' : valor).replace(/[&<>"']/g, c => ({
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#39;'
  })[c]);
}

const ICON_SEARCH = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>';

function renderNavbar({
                        basePath = '',
                        activePage = '',
                        usuario = null
                      } = {}) {
  const container = document.getElementById('app-navbar');

  if (!container) {
    return;
  }

  const linksHTML = NAV_LINKS.map(link => `
    <a href="${basePath}${link.href}"
       class="${link.id === activePage ? 'is-active' : ''}">
      ${link.label}
    </a>
  `).join('');

  let actionsHTML;

  if (usuario) {
    const nome = escaparHTML(usuario.nome || 'Usuário');

    const iniciais = nome
        .trim()
        .split(/\s+/)
        .slice(0, 2)
        .map(p => p.charAt(0))
        .join('')
        .toUpperCase();

    actionsHTML = `
      <a href="${basePath}perfil"
         class="avatar"
         title="Meu perfil"
         aria-label="Meu perfil">
        ${iniciais}
      </a>

      <span class="navbar__user-name">${nome}</span>

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
      <img
        src="${basePath}assets/images/logo-icon.png"
        alt="ReelBook"
      />

      <span class="logo__text">
        <span class="logo__reel">Reel</span>
        <span class="logo__book">Book</span>
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

function criarMovieCardHTML(filme, basePath = '') {
  const id = Number(filme.idFilme || filme.id || 0);

  const titulo = escaparHTML(
      filme.titulo || filme.title || 'Filme sem título'
  );

  const ano = escaparHTML(
      filme.ano || filme.year || ''
  );

  const poster = escaparHTML(
      filme.posterUrl || ''
  );

  const nota = filme.rating != null
      ? `★ ${Number(filme.rating).toFixed(1)}`
      : '';

  const destino = `${basePath}filmes?acao=detalhe&id=${id}`;

  return `
    <a class="movie-card" href="${destino}">
      <img
        class="movie-card__poster"
        src="${poster}"
        alt="Pôster de ${titulo}"
        loading="lazy"
        onerror="this.style.visibility='hidden'"
      />

      <div class="movie-card__info">
        <p class="movie-card__title">${titulo}</p>

        <div class="movie-card__meta">
          <span>${ano}</span>

          ${nota
      ? `<span class="movie-card__rating">${nota}</span>`
      : ''
  }
        </div>
      </div>
    </a>
  `;
}

function renderMovieGrid(container, filmes, basePath = '') {
  if (!container) {
    return;
  }

  if (!filmes || filmes.length === 0) {
    container.innerHTML = `
      <div class="empty-state">
        Nenhum filme encontrado.
      </div>
    `;
    return;
  }

  container.innerHTML = filmes
      .map(filme => criarMovieCardHTML(filme, basePath))
      .join('');
}