
// monta a navbar dentro do <header id="app-navbar">
// basePath: '' se for a home, '../' se a página tiver dentro de /pages
// activePage: qual link fica destacado (home, explorar, listas, diario)
// usuario: o objeto do usuário logado, ou null se for visitante
const NAV_LINKS = [
  { id: 'home', label: 'Início', href: 'index.html' },
  { id: 'explorar', label: 'Explorar', href: 'pages/explorar.html' },
  { id: 'listas', label: 'Listas', href: 'pages/listas.html' },
  { id: 'diario', label: 'Diário', href: 'pages/diario.html' },
];

function renderNavbar({ basePath = '', activePage = '', usuario = null } = {}) {
  const container = document.getElementById('app-navbar');
  if (!container) return; // essa página não tem navbar (tipo uma futura tela de login)

  const linksHTML = NAV_LINKS.map(link => `
    <a href="${basePath}${link.href}" class="${link.id === activePage ? 'is-active' : ''}">
      ${link.label}
    </a>
  `).join('');

  // se não tem usuário salvo, mostra os botões de visitante
  // parte de login trocar isso por uma checagem de sessão de verdade
  const actionsHTML = usuario
    ? `
      <button class="icon-btn" aria-label="Buscar">${ICON_SEARCH}</button>
      <a href="${basePath}pages/perfil.html">
        <img class="avatar" src="${usuario.avatarUrl}" alt="Foto de ${usuario.nome}" />
      </a>
    `
    : `
      <a href="${basePath}pages/login.html" class="btn btn--outline btn--sm">Entrar</a>
      <a href="${basePath}pages/login.html" class="btn btn--primary btn--sm">Criar conta</a>
    `;

  container.innerHTML = `
    <a href="${basePath}index.html" class="logo">
      <img src="${basePath}assets/images/logo-icon.png" alt="ReelBook" />
      <span class="logo__text"><span class="logo__reel">Reel</span><span class="logo__book">Book</span></span>
    </a>
    <nav class="navbar__links">${linksHTML}</nav>
    <div class="navbar__actions">${actionsHTML}</div>
  `;
}

const ICON_SEARCH = `
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
    <circle cx="11" cy="11" r="8" />
    <line x1="21" y1="21" x2="16.65" y2="16.65" />
  </svg>
`;

// monta o card de um filme
// recebe um filme no mesmo formato do MOCK_MOVIES e devolve o HTML
// dá pra usar em qualquer tela que mostre filme: home, explorar, favoritos...
function criarMovieCardHTML(filme, basePath = '') {
  return `
    <a class="movie-card" href="${basePath}pages/filme.html?id=${filme.id}">
      <img class="movie-card__poster" src="${filme.posterUrl}" alt="Pôster de ${filme.title}" loading="lazy" />
      <div class="movie-card__info">
        <p class="movie-card__title">${filme.title}</p>
        <div class="movie-card__meta">
          <span>${filme.year}</span>
          <span class="movie-card__rating">★ ${formatarNota(filme.rating)}</span>
        </div>
      </div>
    </a>
  `;
}

// pega uma lista de filme e joga um monte de card dentro do container
function renderMovieGrid(container, filmes, basePath = '') {
  if (!container) return;

  if (!filmes || filmes.length === 0) {
    container.outerHTML = `<div class="empty-state">Nenhum filme para mostrar ainda.</div>`;
    return;
  }

  container.innerHTML = filmes.map(filme => criarMovieCardHTML(filme, basePath)).join('');
}

// bloqueio de página que só devia funcionar logado
// (favoritos, listas, diário, perfil) — se não tem usuário "falso"
// salvo, mostra um aviso no lugar do conteúdo em vez de deixar
// abrir a página de qualquer jeito
//
// if (!protegerPagina('main-conteudo', '../')) return;
// retorna true se pode seguir (tá "logado"), false se mostrou o aviso
function protegerPagina(idConteudo, basePath = '') {
  const usuario = getUsuarioLogado();
  if (usuario) return true;

  const conteudo = document.getElementById(idConteudo);
  if (conteudo) {
    conteudo.innerHTML = `
      <div class="auth-gate">
        <img src="${basePath}assets/images/logo-icon.png" alt="" class="auth-gate__icon" />
        <h2>Você precisa estar logado para ver esta página</h2>
        <p class="text-muted">Crie uma conta ou entre para acessar suas listas, favoritos e diário.</p>
        <div class="hero__actions" style="justify-content:center; margin-top: var(--space-5);">
          <a href="${basePath}pages/login.html" class="btn btn--primary">Entrar / Criar conta</a>
          <a href="${basePath}index.html" class="btn btn--outline">Voltar para o início</a>
        </div>
        <p class="text-faint" style="margin-top: var(--space-4);">
          (pra testar: abre o dev-login.html e clica em "Simular login")
        </p>
      </div>
    `;
  }
  return false;
}
