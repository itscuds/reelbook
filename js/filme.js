const parametros = new URLSearchParams(window.location.search);

const idFilme = Number(parametros.get('id'));

const filme = MOCK_MOVIES.find(filme => filme.id === idFilme);

console.log('ID do filme:', idFilme);
console.log('Filme encontrado:', filme);

const titulo = document.getElementById('filme-titulo');

titulo.textContent = filme.title;