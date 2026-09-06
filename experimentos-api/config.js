// Credenciais do protótipo.
// Para produção, não exponha tokens no JavaScript do navegador.
// O ideal é colocar a chamada ao TMDB em um backend/proxy.
const TMDB_CONFIG = {
  token: "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzM2IzMjViOTQ4OWRmOTk5MjNmN2E1OTBiNzhjYmQ4MyIsIm5iZiI6MTc4ODQ3NzY1Ni4yMzUwMDAxLCJzdWIiOiI2YTlhMDBkODY3ODk0Zjk1YWYyNGNhN2MiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.fvCLKLoHoLcym9znRPZROGI1oghAt2VwsGbA3RIAFxg",
  baseUrl: "https://api.themoviedb.org/3",
  imageUrl: "https://image.tmdb.org/t/p/w500",
  backdropUrl: "https://image.tmdb.org/t/p/w1280"
};
