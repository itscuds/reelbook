
const MOCK_LISTAS = [
  {
    nome: 'Filmes para assistir',
    total: 15,
    capas: MOCK_MOVIES.slice(0, 4)
  },
  {
    nome: 'Favoritos da vida',
    total: 28,
    capas: MOCK_MOVIES.slice(2, 6)
  },
  {
    nome: 'Clássicos',
    total: 10,
    capas: MOCK_MOVIES.slice(1, 5)
  }
];

// Recupera as listas salvas
const listasSalvas = localStorage.getItem('listas');

let listas = listasSalvas
    ? JSON.parse(listasSalvas)
    : [...MOCK_LISTAS];

// Salva as listas no navegador
function salvarListas() {
  localStorage.setItem('listas', JSON.stringify(listas));
}

// Renderiza todas as listas
function renderizarListas() {

  const container = document.getElementById('listas-container');

  if (!container) return;

  container.innerHTML = listas.map(lista => `
    <div class="lista-card">

      <div class="lista-card__info">

        <p class="lista-card__nome">
          ${lista.nome}
        </p>

        <p class="text-faint">
          ${lista.total} filmes
        </p>

      </div>

      <div class="lista-card__capas">

        ${lista.capas.map(filme => `
          <img src="${filme.posterUrl}" alt="" />
        `).join('')}

      </div>

    </div>
  `).join('');
}

// Inicialização da página
document.addEventListener('DOMContentLoaded', () => {

  const container = document.getElementById('listas-container');

  if (!container) return;

  // Renderiza as listas existentes
  renderizarListas();

  // Botão para criar uma nova lista
  const botaoNovaLista = document.getElementById('btn-nova-lista');

  if (botaoNovaLista) {

    botaoNovaLista.addEventListener('click', () => {

      const nomeLista = prompt('Digite o nome da nova lista:');

      if (!nomeLista || !nomeLista.trim()) return;

      // Adiciona a nova lista
      listas.push({
        nome: nomeLista.trim(),
        total: 0,
        capas: []
      });

      // Salva e atualiza a tela
      salvarListas();
      renderizarListas();

    });

  }

});
