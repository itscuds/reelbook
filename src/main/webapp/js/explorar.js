document.addEventListener('DOMContentLoaded', () => {
    carregarGeneros();
    carregarAnos();
    carregarFilmes();

    const form = document.getElementById('filtros-filmes');

    if (!form) {
        return;
    }

    form.addEventListener('submit', event => {
        event.preventDefault();
        carregarFilmes();
    });

    form.addEventListener('reset', () => {
        setTimeout(carregarFilmes, 0);
    });
});

async function carregarGeneros() {
    const select = document.getElementById('filtro-genero');

    if (!select) {
        return;
    }

    try {
        const base = document.body.dataset.basepath || '';

        const resposta = await fetch(
            `${base}filmes?acao=generos-json`
        );

        if (!resposta.ok) {
            throw new Error(`HTTP ${resposta.status}`);
        }

        const dados = await resposta.json();

        (dados.generos || []).forEach(genero => {
            const option = document.createElement('option');

            option.value = genero.idGenero;
            option.textContent = genero.nome;

            select.appendChild(option);
        });

    } catch (erro) {
        console.error(
            'Erro ao carregar gêneros:',
            erro
        );
    }
}

async function carregarAnos() {
    const select = document.getElementById('filtro-ano');

    if (!select) {
        return;
    }

    try {
        const base = document.body.dataset.basepath || '';

        const resposta = await fetch(
            `${base}filmes?acao=json`
        );

        if (!resposta.ok) {
            throw new Error(`HTTP ${resposta.status}`);
        }

        const dados = await resposta.json();

        const anos = [
            ...new Set(
                (dados.filmes || [])
                    .map(filme => Number(filme.ano))
                    .filter(Boolean)
            )
        ].sort((a, b) => b - a);

        anos.forEach(ano => {
            const option = document.createElement('option');

            option.value = ano;
            option.textContent = ano;

            select.appendChild(option);
        });

    } catch (erro) {
        console.error(
            'Erro ao carregar anos:',
            erro
        );
    }
}

async function carregarFilmes() {
    const base = document.body.dataset.basepath || '';

    const grid = document.getElementById('resultados-grid');

    if (!grid) {
        return;
    }

    const params = new URLSearchParams({
        acao: 'json'
    });

    const campoBusca = document.getElementById('busca-filme');
    const campoAno = document.getElementById('filtro-ano');
    const campoGenero = document.getElementById('filtro-genero');

    const busca = campoBusca ? campoBusca.value.trim() : '';
    const ano = campoAno ? campoAno.value : '';
    const genero = campoGenero ? campoGenero.value : '';

    if (busca) {
        params.set('busca', busca);
    }

    if (ano) {
        params.set('ano', ano);
    }

    if (genero) {
        params.set('genero', genero);
    }

    grid.innerHTML = `
    <p class="text-faint">
      Carregando filmes...
    </p>
  `;

    try {
        const resposta = await fetch(
            `${base}filmes?${params.toString()}`,
            {
                cache: 'no-store'
            }
        );

        if (!resposta.ok) {
            throw new Error(`HTTP ${resposta.status}`);
        }

        const dados = await resposta.json();
        const filmes = dados.filmes || [];

        renderMovieGrid(grid, filmes, base);

        const contagem = document.getElementById(
            'resultado-contagem'
        );

        if (contagem) {
            contagem.textContent =
                `${filmes.length} filme(s) encontrado(s)`;
        }

    } catch (erro) {
        console.error(
            'Erro ao buscar filmes:',
            erro
        );

        grid.innerHTML = `
      <div class="empty-state">
        Não foi possível carregar o catálogo.
        Verifique se o servidor e o banco estão funcionando.
      </div>
    `;
    }
}