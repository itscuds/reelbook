/* =============================================================
   main.js — carrega em toda página do site

   O que tem aqui:
     1. os filmes falsos (até conectar a API de verdade)
     2. um "login" falso guardado no localStorage, só pra dar pra
        testar como fica logado x visitante
     3. o bootstrap que chama a navbar quando a página carrega

   nada disso é definitivo
   ============================================================= */

// Joāo trocar isso pelos dados de verdade da API do TMDb —
// só mantém o mesmo formato (id, title, year, rating, posterUrl)
// que os cards continuam funcionando sem precisar mexer em mais nada
const MOCK_MOVIES = [
  {
    id: 1,
    title: 'Duna: Parte Dois',
    year: 2024,
    rating: 4.5,
    posterUrl: 'https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg',
    duration: '2h 46min',
    genres: ['Ficção científica', 'Aventura', 'Drama'],
    synopsis: 'Paul Atreides se une a Chani e aos Fremen enquanto busca vingança contra os conspiradores que destruíram sua família.',
    director: 'Denis Villeneuve',
    writer: 'Jon Spaihts, Denis Villeneuve, Craig Mazin',
    country: 'EUA, Canadá'
  },
  {
    id: 2,
    title: 'Interestelar',
    year: 2014,
    rating: 4.7,
    posterUrl: 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg',
    duration: '2h 49min',
    genres: ['Ficção científica', 'Drama', 'Aventura'],
    synopsis: 'Um grupo de exploradores viaja através de um buraco de minhoca em uma tentativa de garantir a sobrevivência da humanidade.',
    director: 'Christopher Nolan',
    writer: 'Jonathan Nolan, Christopher Nolan',
    country: 'EUA, Reino Unido'
  },
  {
    id: 3,
    title: 'Pobres Criaturas',
    year: 2023,
    rating: 4.2,
    posterUrl: 'https://image.tmdb.org/t/p/original/9OYMVcP2zyw0zpWOTuxlDo2MsMw.jpg',
    duration: '2h 21min',
    genres: ['Comédia', 'Drama', 'Ficção científica'],
    synopsis: 'Uma jovem é trazida de volta à vida por um cientista excêntrico e embarca em uma jornada de descoberta e liberdade.',
    director: 'Yorgos Lanthimos',
    writer: 'Tony McNamara',
    country: 'Irlanda, Reino Unido, EUA'
  },
  {
    id: 4,
    title: 'Oppenheimer',
    year: 2023,
    rating: 4.6,
    posterUrl: 'https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg',
    duration: '3h 1min',
    genres: ['Drama', 'História'],
    synopsis: 'A história de J. Robert Oppenheimer e seu papel no desenvolvimento da primeira bomba atômica.',
    director: 'Christopher Nolan',
    writer: 'Christopher Nolan',
    country: 'EUA, Reino Unido'
  },
  {
    id: 5,
    title: 'Barbie',
    year: 2023,
    rating: 3.8,
    posterUrl: 'https://image.tmdb.org/t/p/w500/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg',
    duration: '1h 54min',
    genres: ['Comédia', 'Aventura', 'Fantasia'],
    synopsis: 'Barbie deixa Barbieland e parte para o mundo real em uma jornada que transforma sua visão sobre a própria existência.',
    director: 'Greta Gerwig',
    writer: 'Greta Gerwig, Noah Baumbach',
    country: 'EUA, Reino Unido'
  },
  {
    id: 6,
    title: 'Clube da Luta',
    year: 1999,
    rating: 4.6,
    posterUrl: 'https://image.tmdb.org/t/p/w500/bptfVGEQuv6vDTIMVCHjJ9Dz8PX.jpg',
    duration: '2h 19min',
    genres: ['Drama'],
    synopsis: 'Um homem insatisfeito com sua vida conhece Tyler Durden e acaba envolvido em um clube clandestino de luta.',
    director: 'David Fincher',
    writer: 'Jim Uhls',
    country: 'EUA, Alemanha'
  },
  {
    id: 7,
    title: 'A Origem',
    year: 2010,
    rating: 4.4,
    posterUrl: 'https://image.tmdb.org/t/p/w500/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg',
    duration: '2h 28min',
    genres: ['Ação', 'Ficção científica', 'Aventura'],
    synopsis: 'Um especialista em extrair informações através dos sonhos recebe a missão de implantar uma ideia na mente de um alvo.',
    director: 'Christopher Nolan',
    writer: 'Christopher Nolan',
    country: 'EUA, Reino Unido'
  },
  {
    id: 8,
    title: 'Forrest Gump',
    year: 1994,
    rating: 4.5,
    posterUrl: 'https://image.tmdb.org/t/p/w500/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg',
    duration: '2h 22min',
    genres: ['Comédia', 'Drama', 'Romance'],
    synopsis: 'Forrest Gump atravessa décadas da história americana enquanto vive uma vida extraordinária sem deixar de lado seu jeito simples de enxergar o mundo.',
    director: 'Robert Zemeckis',
    writer: 'Eric Roth',
    country: 'EUA'
  },
  {
    id: 9,
    title: 'Batman: O Cavaleiro das Trevas',
    year: 2008,
    rating: 4.8,
    posterUrl: 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg',
    duration: '2h 32min',
    genres: ['Drama', 'Ação', 'Crime'],
    synopsis: 'Batman enfrenta uma ameaça criminosa que mergulha Gotham em um caos cada vez maior.',
    director: 'Christopher Nolan',
    writer: 'Jonathan Nolan, Christopher Nolan, David S. Goyer',
    country: 'EUA, Reino Unido'
  },
  {
    id: 10,
    title: 'Matrix',
    year: 1999,
    rating: 4.6,
    posterUrl: 'https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg',
    duration: '2h 16min',
    genres: ['Ação', 'Ficção científica'],
    synopsis: 'Um programador descobre que a realidade como conhece é uma simulação e se junta à resistência contra as máquinas.',
    director: 'Lana Wachowski, Lilly Wachowski',
    writer: 'Lana Wachowski, Lilly Wachowski',
    country: 'EUA, Austrália'
  }
];


// quando o login de verdade existir, é só trocar
// essas duas funções aqui embaixo

// pra testar sem mexer no código: abre o arquivo dev-login.html
const AUTH_STORAGE_KEY = 'reelbook_mock_user';

function getUsuarioLogado() {
  const salvo = localStorage.getItem(AUTH_STORAGE_KEY);
  return salvo ? JSON.parse(salvo) : null; // null = visitante (não logado)
}

function definirUsuarioLogado(usuario) {
  if (usuario) {
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(usuario));
  } else {
    localStorage.removeItem(AUTH_STORAGE_KEY);
  }
}

function formatarNota(nota) {
  return typeof nota === 'number' ? nota.toFixed(1) : '—';
}

// isso roda em toda página assim que carrega: pega o data-page e o
// data-basepath que tão no <body> e manda a navbar se montar
// (a função renderNavbar em components.js)
document.addEventListener('DOMContentLoaded', () => {
  const { page = '', basepath = '' } = document.body.dataset;

  renderNavbar({
    basePath: basepath,
    activePage: page,
    usuario: getUsuarioLogado(),
  });
});
