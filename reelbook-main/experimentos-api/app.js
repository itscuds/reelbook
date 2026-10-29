const API = TMDB_CONFIG;
const state = {
  page: 1,
  mode: "popular",
  query: "",
  totalPages: 1,
  genres: [],
  movies: []
};

const $ = (id) => document.getElementById(id);
const movieGrid = $("movieGrid");
const statusBox = $("status");

const genreNames = {
  28: "Ação", 12: "Aventura", 16: "Animação", 35: "Comédia", 80: "Crime",
  99: "Documentário", 18: "Drama", 10751: "Família", 14: "Fantasia",
  36: "História", 27: "Terror", 10402: "Música", 9648: "Mistério",
  10749: "Romance", 878: "Ficção científica", 10770: "Cinema TV",
  53: "Thriller", 10752: "Guerra", 37: "Faroeste"
};

async function api(path, params = {}) {
  const url = new URL(API.baseUrl + path);
  Object.entries(params).forEach(([key, value]) => {
    if (value !== "" && value !== null && value !== undefined) url.searchParams.set(key, value);
  });

  const response = await fetch(url, {
    headers: {
      Authorization: `Bearer ${API.token}`,
      accept: "application/json"
    }
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(`TMDB ${response.status}: ${text}`);
  }
  return response.json();
}

function image(path, size = API.imageUrl) {
  return path ? `${size}${path}` : "data:image/svg+xml;charset=UTF-8," + encodeURIComponent(`
    <svg xmlns="http://www.w3.org/2000/svg" width="500" height="750" viewBox="0 0 500 750">
      <rect width="500" height="750" fill="#17151c"/>
      <text x="250" y="370" text-anchor="middle" fill="#8f8a98" font-family="sans-serif" font-size="22">Sem pôster</text>
    </svg>`);
}

function escapeHtml(value = "") {
  return String(value).replace(/[&<>"']/g, c => ({
    "&":"&amp;", "<":"&lt;", ">":"&gt;", '"':"&quot;", "'":"&#039;"
  }[c]));
}

function yearOf(movie) {
  return (movie.release_date || movie.first_air_date || "").slice(0, 4) || "—";
}

function rating(movie) {
  return Number(movie.vote_average || 0).toFixed(1);
}

function isFavorite(id) {
  return getFavorites().some(m => m.id === id);
}

function getFavorites() {
  try { return JSON.parse(localStorage.getItem("reelbook:favorites") || "[]"); }
  catch { return []; }
}

function setFavorites(list) {
  localStorage.setItem("reelbook:favorites", JSON.stringify(list));
  renderFavorites();
  updateFavoriteCount();
}

function toggleFavorite(movie) {
  const favorites = getFavorites();
  const exists = favorites.some(m => m.id === movie.id);
  const next = exists ? favorites.filter(m => m.id !== movie.id) : [movie, ...favorites];
  setFavorites(next);
  renderMovies(state.movies);
  renderFavorites();
}

function updateFavoriteCount() {
  $("favoriteCount").textContent = getFavorites().length;
}

function movieCard(movie) {
  const favorite = isFavorite(movie.id);
  return `
    <article class="movie-card" data-id="${movie.id}">
      <button class="favorite-button ${favorite ? "is-favorite" : ""}" data-favorite="${movie.id}" title="${favorite ? "Remover dos favoritos" : "Adicionar aos favoritos"}" aria-label="${favorite ? "Remover dos favoritos" : "Adicionar aos favoritos"}">
        ${favorite ? "♥" : "♡"}
      </button>
      <button class="poster-button" data-details="${movie.id}" aria-label="Ver detalhes de ${escapeHtml(movie.title)}">
        <img src="${image(movie.poster_path)}" alt="" loading="lazy">
        <span class="poster-overlay">Ver detalhes</span>
      </button>
      <div class="movie-meta">
        <div class="movie-title-row">
          <h3>${escapeHtml(movie.title)}</h3>
          <span class="rating">★ ${rating(movie)}</span>
        </div>
        <p>${yearOf(movie)} · ${(movie.genre_ids || []).slice(0, 2).map(id => genreNames[id]).filter(Boolean).join(" · ") || "Filme"}</p>
      </div>
    </article>`;
}

function renderMovies(movies) {
  if (!movies.length) {
    movieGrid.innerHTML = "";
    statusBox.hidden = false;
    statusBox.textContent = "Nenhum filme encontrado com esses filtros.";
    return;
  }
  statusBox.hidden = true;
  movieGrid.innerHTML = movies.map(movieCard).join("");
}

function renderFavorites() {
  const favorites = getFavorites();
  const grid = $("favoritesGrid");
  $("emptyFavorites").hidden = favorites.length > 0;
  grid.innerHTML = favorites.map(movieCard).join("");
}

async function loadGenres() {
  const data = await api("/genre/movie/list", { language: "pt-BR" });
  state.genres = data.genres || [];
  $("genreFilter").insertAdjacentHTML(
    "beforeend",
    state.genres.map(g => `<option value="${g.id}">${escapeHtml(g.name)}</option>`).join("")
  );
}

function populateYears() {
  const current = new Date().getFullYear();
  $("yearFilter").insertAdjacentHTML(
    "beforeend",
    Array.from({ length: 31 }, (_, i) => current - i)
      .map(y => `<option value="${y}">${y}</option>`).join("")
  );
}

async function loadMovies() {
  showLoading();
  try {
    const params = {
      language: "pt-BR",
      region: "BR",
      page: state.page,
      include_adult: false,
      sort_by: $("sortFilter").value
    };

    let data;
    if (state.mode === "search" && state.query) {
      data = await api("/search/movie", {
        query: state.query,
        language: "pt-BR",
        region: "BR",
        page: state.page,
        include_adult: false
      });
      $("sectionTitle").textContent = `Resultados para “${state.query}”`;
    } else {
      const genre = $("genreFilter").value;
      const year = $("yearFilter").value;
      const ratingMin = $("ratingFilter").value;

      if (genre) params.with_genres = genre;
      if (year) params.primary_release_year = year;
      if (ratingMin) {
        params["vote_average.gte"] = ratingMin;
        params["vote_count.gte"] = 50;
      }
      data = await api("/discover/movie", params);
      $("sectionTitle").textContent = "Filmes populares";
    }

    state.movies = data.results || [];
    state.totalPages = Math.min(data.total_pages || 1, 500);
    renderMovies(state.movies);
    $("pageInfo").textContent = `Página ${state.page} de ${state.totalPages}`;
    $("prevPage").disabled = state.page <= 1;
    $("nextPage").disabled = state.page >= state.totalPages;
    $("resultInfo").textContent = `${data.total_results || 0} resultados`;
    window.scrollTo({ top: document.querySelector(".catalog").offsetTop - 70, behavior: "smooth" });
  } catch (error) {
    statusBox.hidden = false;
    statusBox.textContent = "Não foi possível carregar os filmes. Verifique o token e sua conexão.";
    movieGrid.innerHTML = "";
    console.error(error);
  }
}

function showLoading() {
  statusBox.hidden = false;
  statusBox.textContent = "Carregando filmes...";
  movieGrid.innerHTML = Array.from({length: 8}, () => `<div class="skeleton-card"><div></div><span></span><i></i></div>`).join("");
}

async function openDetails(id) {
  const modal = $("movieModal");
  const content = $("modalContent");
  modal.classList.add("open");
  modal.setAttribute("aria-hidden", "false");
  content.innerHTML = `<div class="modal-loading">Carregando detalhes...</div>`;

  try {
    const movie = await api(`/movie/${id}`, {
      language: "pt-BR",
      append_to_response: "credits,videos"
    });

    const trailer = (movie.videos?.results || []).find(v => v.site === "YouTube" && v.type === "Trailer");
    const director = (movie.credits?.crew || []).find(p => p.job === "Director");
    const cast = (movie.credits?.cast || []).slice(0, 5).map(p => p.name).join(", ");

    content.innerHTML = `
      <div class="detail-hero" style="--backdrop:url('${movie.backdrop_path ? image(movie.backdrop_path, API.backdropUrl) : image(movie.poster_path)}')">
        <img class="detail-poster" src="${image(movie.poster_path)}" alt="Pôster de ${escapeHtml(movie.title)}">
        <div class="detail-copy">
          <div class="detail-badge">TMDB ${rating(movie)}</div>
          <h2>${escapeHtml(movie.title)}</h2>
          ${movie.tagline ? `<p class="tagline">${escapeHtml(movie.tagline)}</p>` : ""}
          <p class="detail-facts">${yearOf(movie)} · ${movie.runtime ? `${movie.runtime} min` : "Duração indisponível"} · ${(movie.genres || []).slice(0, 3).map(g => escapeHtml(g.name)).join(" · ")}</p>
          <p class="overview">${escapeHtml(movie.overview || "Sinopse indisponível.")}</p>
          ${director ? `<p class="credit"><strong>Direção:</strong> ${escapeHtml(director.name)}</p>` : ""}
          ${cast ? `<p class="credit"><strong>Elenco:</strong> ${escapeHtml(cast)}</p>` : ""}
          <div class="detail-actions">
            <button class="primary-button" data-modal-favorite="${movie.id}">
              ${isFavorite(movie.id) ? "♥ Remover dos favoritos" : "♡ Adicionar aos favoritos"}
            </button>
            ${trailer ? `<a class="secondary-button" href="https://www.youtube.com/watch?v=${trailer.key}" target="_blank" rel="noopener">▶ Trailer</a>` : ""}
          </div>
        </div>
      </div>`;
    content.querySelector("[data-modal-favorite]")?.addEventListener("click", () => {
      toggleFavorite(movie);
      openDetails(movie.id);
    });
  } catch (error) {
    content.innerHTML = `<div class="modal-loading">Não foi possível carregar os detalhes.</div>`;
    console.error(error);
  }
}

function closeModal() {
  $("movieModal").classList.remove("open");
  $("movieModal").setAttribute("aria-hidden", "true");
}

document.addEventListener("click", event => {
  const details = event.target.closest("[data-details]");
  const favorite = event.target.closest("[data-favorite]");

  if (details) openDetails(Number(details.dataset.details));
  if (favorite) {
    const movie = [...state.movies, ...getFavorites()].find(m => m.id === Number(favorite.dataset.favorite));
    if (movie) toggleFavorite(movie);
  }

  if (event.target.matches("[data-close-modal]")) closeModal();
});

$("searchForm").addEventListener("submit", event => {
  event.preventDefault();
  const query = $("searchInput").value.trim();
  state.query = query;
  state.mode = query ? "search" : "popular";
  state.page = 1;
  loadMovies();
});

["genreFilter", "yearFilter", "ratingFilter", "sortFilter"].forEach(id => {
  $(id).addEventListener("change", () => {
    state.mode = "popular";
    state.query = "";
    $("searchInput").value = "";
    state.page = 1;
    loadMovies();
  });
});

$("clearFilters").addEventListener("click", () => {
  ["genreFilter", "yearFilter", "ratingFilter"].forEach(id => $(id).value = "");
  $("sortFilter").value = "popularity.desc";
  state.mode = "popular";
  state.query = "";
  state.page = 1;
  $("searchInput").value = "";
  loadMovies();
});

$("prevPage").addEventListener("click", () => {
  if (state.page > 1) { state.page--; loadMovies(); }
});

$("nextPage").addEventListener("click", () => {
  if (state.page < state.totalPages) { state.page++; loadMovies(); }
});

$("themeToggle").addEventListener("click", () => {
  document.body.classList.toggle("light-theme");
  localStorage.setItem("reelbook:theme", document.body.classList.contains("light-theme") ? "light" : "dark");
});

if (localStorage.getItem("reelbook:theme") === "light") document.body.classList.add("light-theme");

document.addEventListener("keydown", event => {
  if (event.key === "Escape") closeModal();
});

populateYears();
updateFavoriteCount();
renderFavorites();
Promise.all([loadGenres(), loadMovies()]).catch(console.error);
