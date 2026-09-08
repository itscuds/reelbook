/* =============================================================
   home.js — lógica da página inicial

   faz duas coisas:
     1. monta o grid "Em destaque" com os filmes
     2. troca o texto do hero dependendo se tá "logado" ou não

   usa coisas de main.js (MOCK_MOVIES, getUsuarioLogado) e de
   components.js (renderMovieGrid) por isso a ordem dos
   scripts no index.html importa
   ============================================================= */

document.addEventListener('DOMContentLoaded', () => {
  renderizarHero();
  renderizarFilmesEmDestaque();
});

// muda o texto do hero dependendo se tem usuário "logado" ou não
// TODO(quem for fazer o login): troca o getUsuarioLogado() por uma
// checagem de sessão de verdade quando o login existir
function renderizarHero() {
  const heroText = document.getElementById('hero-text');
  const usuario = getUsuarioLogado();

  heroText.innerHTML = usuario
    ? `
      <h1>Bem-vindo de volta, ${usuario.nome}!</h1>
      <p>Continue de onde parou: descubra, organize e compartilhe sua paixão por filmes.</p>
      <div class="hero__actions">
        <a href="pages/explorar.html" class="btn btn--primary">Explorar filmes</a>
        <a href="pages/listas.html" class="btn btn--outline">Minhas listas</a>
      </div>
    `
    : `
      <h1>Descubra, avalie e organize os filmes que você assiste</h1>
      <p>Crie sua conta para montar suas listas, registrar seu diário e favoritar os filmes que você ama.</p>
      <div class="hero__actions">
        <a href="pages/login.html" class="btn btn--primary">Criar conta</a>
        <a href="pages/explorar.html" class="btn btn--outline">Explorar sem cadastro</a>
      </div>
    `;
}

// monta o grid de filmes em destaque
// troca o MOCK_MOVIES pelos filmes populares vindos
// da API do TMDb — o grid e o card já esperam o mesmo formato de
// objeto, então é só trocar essa linha aqui:
function renderizarFilmesEmDestaque() {
  const grid = document.getElementById('destaque-grid');
  renderMovieGrid(grid, MOCK_MOVIES, '');
}
