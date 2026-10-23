const parametros = new URLSearchParams(window.location.search);

const idFilme = Number(parametros.get('id'));

const filme = MOCK_MOVIES.find(filme => filme.id === idFilme);

console.log('ID do filme:', idFilme);
console.log('Filme encontrado:', filme);

const titulo = document.getElementById('filme-titulo');

titulo.textContent = filme.title;

const sinopse = document.getElementById('filme-sinopse');

sinopse.textContent = filme.synopsis;

const info = document.getElementById('filme-info');

info.textContent = `${filme.year} · ${filme.duration} · ${filme.genres.join(', ')}`;

const nota = document.getElementById('filme-nota');

nota.textContent = `★ ${filme.rating}`;

const poster = document.getElementById('filme-poster');

poster.src = filme.posterUrl;
poster.alt = `Pôster de ${filme.title}`;

const diretor = document.getElementById('filme-diretor');

diretor.textContent = filme.director;

const roteiro = document.getElementById('filme-roteiro');

roteiro.textContent = filme.writer;

const pais = document.getElementById('filme-pais');

pais.textContent = filme.country;