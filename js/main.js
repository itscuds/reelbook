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
  { id: 1, title: 'Duna: Parte Dois', year: 2024, rating: 4.5, posterUrl: 'https://placehold.co/300x450/1e1e2a/a78bfa?font=poppins&text=Duna%3A+Parte+Dois' },
  { id: 2, title: 'Interestelar', year: 2014, rating: 4.7, posterUrl: 'https://placehold.co/300x450/1e1e2a/a78bfa?font=poppins&text=Interestelar' },
  { id: 3, title: 'Pobres Criaturas', year: 2023, rating: 4.2, posterUrl: 'https://placehold.co/300x450/1e1e2a/a78bfa?font=poppins&text=Pobres+Criaturas' },
  { id: 4, title: 'Oppenheimer', year: 2023, rating: 4.6, posterUrl: 'https://placehold.co/300x450/1e1e2a/a78bfa?font=poppins&text=Oppenheimer' },
  { id: 5, title: 'Barbie', year: 2023, rating: 3.8, posterUrl: 'https://placehold.co/300x450/1e1e2a/a78bfa?font=poppins&text=Barbie' },
  { id: 6, title: 'Clube da Luta', year: 1999, rating: 4.6, posterUrl: 'https://placehold.co/300x450/1e1e2a/a78bfa?font=poppins&text=Clube+da+Luta' },
  { id: 7, title: 'A Origem', year: 2010, rating: 4.4, posterUrl: 'https://placehold.co/300x450/1e1e2a/a78bfa?font=poppins&text=A+Origem' },
  { id: 8, title: 'Forrest Gump', year: 1994, rating: 4.5, posterUrl: 'https://placehold.co/300x450/1e1e2a/a78bfa?font=poppins&text=Forrest+Gump' },
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
