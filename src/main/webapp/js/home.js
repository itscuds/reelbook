
// =============================================================
// home.js
// Lógica da página inicial
// =============================================================

document.addEventListener('DOMContentLoaded', () => {

  renderizarFilmesEmDestaque();

});

// Aguarda a consulta da sessão real
document.addEventListener('sessaoCarregada', () => {

  renderizarHero();

});

// Renderiza a mensagem de boas-vindas
function renderizarHero() {

  const heroText = document.getElementById('hero-text');

  if (!heroText) return;

  const usuario = getUsuarioLogado();

  if (usuario) {

    heroText.innerHTML = `
      <h1>Bem-vindo de volta, ${usuario.nome}!</h1>

      <p>
        Continue de onde parou: descubra, organize e compartilhe
        sua paixão por filmes.
      </p>

      <div class="hero__actions">

        <a href="pages/explorar.jsp"
           class="btn btn--primary">
          Explorar filmes
        </a>

        <a href="pages/listas.jsp"
           class="btn btn--outline">
          Minhas listas
        </a>

      </div>
    `;

  } else {

    heroText.innerHTML = `
      <h1>
        Descubra, avalie e organize os filmes que você assiste
      </h1>

      <p>
        Crie sua conta para montar suas listas, registrar seu diário
        e favoritar os filmes que você ama.
      </p>

      <div class="hero__actions">

        <a href="pages/cadastro.jsp"
           class="btn btn--primary">
          Criar conta
        </a>

        <a href="pages/explorar.jsp"
           class="btn btn--outline">
          Explorar sem cadastro
        </a>

      </div>
    `;

  }
}

// Renderiza os filmes em destaque
function renderizarFilmesEmDestaque() {

  const grid = document.getElementById('destaque-grid');

  renderMovieGrid(grid, MOCK_MOVIES, '');

}
