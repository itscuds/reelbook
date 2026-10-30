document.addEventListener('DOMContentLoaded', () => {
  carregarDestaques();
});

document.addEventListener('sessaoCarregada', () => {
  renderizarHero();
});

function renderizarHero() {
  const heroText = document.getElementById('hero-text');

  if (!heroText) {
    return;
  }

  const usuario = getUsuarioLogado();

  if (usuario) {
    heroText.innerHTML = `
      <h1>Bem-vindo de volta, ${escaparHTML(usuario.nome)}!</h1>

      <p>
        Continue de onde parou: descubra, organize e compartilhe
        sua paixão por filmes.
      </p>

      <div class="hero__actions">
        <a href="filmes?acao=explorar" class="btn btn--primary">
          Explorar filmes
        </a>

        <a href="pages/listas.jsp" class="btn btn--outline">
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
        <a href="pages/cadastro.jsp" class="btn btn--primary">
          Criar conta
        </a>

        <a href="filmes?acao=explorar" class="btn btn--outline">
          Explorar sem cadastro
        </a>
      </div>
    `;
  }
}

async function carregarDestaques() {
  const grid = document.getElementById('destaque-grid');

  if (!grid) {
    return;
  }

  try {
    const resposta = await fetch(
        'filmes?acao=json',
        {
          cache: 'no-store'
        }
    );

    if (!resposta.ok) {
      throw new Error(`HTTP ${resposta.status}`);
    }

    const dados = await resposta.json();

    renderMovieGrid(
        grid,
        (dados.filmes || []).slice(0, 6),
        ''
    );

  } catch (erro) {
    console.error(
        'Erro ao carregar filmes da Home:',
        erro
    );

    // Mantém o conteúdo demonstrativo visível
    // caso o banco esteja indisponível.
    renderMovieGrid(
        grid,
        MOCK_MOVIES.slice(0, 6),
        ''
    );
  }
}