const MOCK_LISTAS = [
  { nome: 'Filmes para assistir', total: 15, capas: MOCK_MOVIES.slice(0, 4) },
  { nome: 'Favoritos da vida', total: 28, capas: MOCK_MOVIES.slice(2, 6) },
  { nome: 'Clássicos', total: 10, capas: MOCK_MOVIES.slice(1, 5) },
];

const listasSalvas = localStorage.getItem('listas');

let listas = listasSalvas
  ? JSON.parse(listasSalvas)
  : [...MOCK_LISTAS];

document.addEventListener('DOMContentLoaded', () => {
  const logado = protegerPagina('main-conteudo', '../');

  if (!logado) return;

  const container = document.getElementById('listas-container');

  container.innerHTML = listas.map(lista => `
    <div class="lista-card">
      <div class="lista-card__info">
        <p class="lista-card__nome">${lista.nome}</p>
        <p class="text-faint">${lista.total} filmes</p>
      </div>

      <div class="lista-card__capas">
        ${lista.capas.map(f => `<img src="${f.posterUrl}" alt="" />`).join('')}
      </div>
    </div>
  `).join('');
});

const botaoNovaLista = document.getElementById('btn-nova-lista');

botaoNovaLista.addEventListener('click', () => {
  const nomeLista = prompt('Digite o nome da nova lista:');

  if (nomeLista) {
    listas.push({
      nome: nomeLista,
      total: 0,
      capas: []
    });

    localStorage.setItem('listas', JSON.stringify(listas));

    const novaLista = document.createElement('div');

    novaLista.classList.add('lista-card');

    novaLista.innerHTML = `
      <div class="lista-card__info">
        <p class="lista-card__nome">${nomeLista}</p>
        <p class="text-faint">0 filmes</p>
      </div>
    `;

    document.getElementById('listas-container').appendChild(novaLista);
  }
});