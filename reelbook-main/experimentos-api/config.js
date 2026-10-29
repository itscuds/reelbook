// Credenciais do protótipo.
// Para produção, não exponha tokens no JavaScript do navegador.
// O ideal é colocar a chamada ao TMDB em um backend/proxy.
const TMDB_CONFIG = {
  baseUrl: "https://api.themoviedb.org/3",
  imageUrl: "https://image.tmdb.org/t/p/w500",
  backdropUrl: "https://image.tmdb.org/t/p/w1280"
};
