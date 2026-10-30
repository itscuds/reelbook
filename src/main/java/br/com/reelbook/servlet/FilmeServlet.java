package br.com.reelbook.servlet;

import br.com.reelbook.dao.FilmeDAO;
import br.com.reelbook.model.Filme;
import br.com.reelbook.model.Genero;
import br.com.reelbook.util.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/filmes")
public class FilmeServlet extends HttpServlet {

    private final FilmeDAO dao = new FilmeDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String acao = valor(request.getParameter("acao"), "listar");

        try {
            switch (acao) {
                case "json":
                    listarFilmesJson(request, response);
                    return;

                case "generos-json":
                    listarGenerosJson(response);
                    return;

                case "explorar":
                    request.getRequestDispatcher("/pages/explorar.jsp")
                            .forward(request, response);
                    return;

                case "novo":
                    carregarGeneros(request);
                    request.setAttribute("filme", new Filme());
                    request.getRequestDispatcher("/pages/formulario.jsp")
                            .forward(request, response);
                    return;

                case "editar":
                    Filme editar = dao.buscarPorId(
                            inteiro(request.getParameter("id"), -1)
                    );

                    if (editar == null || !editar.isAtivo()) {
                        response.sendError(404, "Filme não encontrado");
                        return;
                    }

                    carregarGeneros(request);
                    request.setAttribute("filme", editar);
                    request.getRequestDispatcher("/pages/formulario.jsp")
                            .forward(request, response);
                    return;

                case "detalhe":
                    Filme detalhe = dao.buscarPorId(
                            inteiro(request.getParameter("id"), -1)
                    );

                    if (detalhe == null || !detalhe.isAtivo()) {
                        response.sendError(404, "Filme não encontrado");
                        return;
                    }

                    request.setAttribute("filme", detalhe);
                    request.getRequestDispatcher("/pages/detalhe.jsp")
                            .forward(request, response);
                    return;

                default:
                    String busca = request.getParameter("busca");
                    Integer ano = inteiroOpcional(request.getParameter("ano"));
                    Integer genero = inteiroOpcional(request.getParameter("genero"));

                    boolean incluirInativos =
                            "true".equals(request.getParameter("inativos"));

                    request.setAttribute(
                            "filmes",
                            dao.listar(busca, ano, genero, incluirInativos)
                    );

                    request.setAttribute("busca", busca == null ? "" : busca);
                    request.setAttribute("anoSelecionado", ano);
                    request.setAttribute("generoSelecionado", genero);
                    request.setAttribute("generos", dao.listarGenerosAtivos());
                    request.setAttribute("anos", listarAnos());
                    request.setAttribute("incluirInativos", incluirInativos);

                    request.getRequestDispatcher("/pages/lista.jsp")
                            .forward(request, response);
            }

        } catch (SQLException e) {
            throw new ServletException("Erro ao consultar filmes", e);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String acao = valor(request.getParameter("acao"), "salvar");

        try {
            if ("excluir".equals(acao)) {
                int id = inteiro(request.getParameter("id"), -1);

                if (id <= 0 || dao.buscarPorId(id) == null) {
                    response.sendError(404, "Filme não encontrado");
                    return;
                }

                dao.desativar(id);
                response.sendRedirect(
                        request.getContextPath() + "/filmes?mensagem=desativado"
                );
                return;
            }

            Filme filme = new Filme();

            filme.setIdFilme(inteiro(request.getParameter("id"), 0));
            filme.setTitulo(valor(request.getParameter("titulo"), "").trim());
            filme.setSinopse(valor(request.getParameter("sinopse"), "").trim());
            filme.setAno(inteiro(request.getParameter("ano"), 0));
            filme.setDuracao(inteiro(request.getParameter("duracao"), 0));
            filme.setPosterUrl(valor(request.getParameter("posterUrl"), "").trim());

            List<Integer> generos = new ArrayList<>();
            String[] selecionados = request.getParameterValues("generos");

            if (selecionados != null) {
                for (String selecionado : selecionados) {
                    int idGenero = inteiro(selecionado, -1);

                    if (idGenero > 0 && !generos.contains(idGenero)) {
                        generos.add(idGenero);
                    }
                }
            }

            String erro = validar(filme);

            if (erro != null) {
                carregarGeneros(request);

                filme.setGeneros(
                        dao.listarGenerosAtivos()
                                .stream()
                                .filter(g -> generos.contains(g.getIdGenero()))
                                .collect(Collectors.toList())
                );

                request.setAttribute("filme", filme);
                request.setAttribute("erro", erro);

                request.getRequestDispatcher("/pages/formulario.jsp")
                        .forward(request, response);
                return;
            }

            if (filme.getIdFilme() > 0) {
                dao.atualizar(filme, generos);
            } else {
                dao.salvar(filme, generos);
            }

            response.sendRedirect(
                    request.getContextPath() + "/filmes?mensagem=salvo"
            );

        } catch (SQLException e) {
            throw new ServletException("Erro ao salvar filme", e);
        }
    }

    private void listarFilmesJson(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, IOException {

        String busca = request.getParameter("busca");
        Integer ano = inteiroOpcional(request.getParameter("ano"));
        Integer genero = inteiroOpcional(request.getParameter("genero"));

        List<Filme> filmes = dao.listar(busca, ano, genero, false);

        StringBuilder json = new StringBuilder("{\"filmes\":[");

        for (int i = 0; i < filmes.size(); i++) {
            Filme f = filmes.get(i);

            if (i > 0) {
                json.append(',');
            }

            json.append('{')
                    .append("\"idFilme\":").append(f.getIdFilme()).append(',')
                    .append("\"titulo\":\"").append(escaparJson(f.getTitulo())).append("\",")
                    .append("\"sinopse\":\"").append(escaparJson(f.getSinopse())).append("\",")
                    .append("\"ano\":").append(f.getAno()).append(',')
                    .append("\"duracao\":").append(f.getDuracao()).append(',')
                    .append("\"posterUrl\":\"").append(escaparJson(f.getPosterUrl())).append("\",")
                    .append("\"generos\":[");

            for (int j = 0; j < f.getGeneros().size(); j++) {
                Genero g = f.getGeneros().get(j);

                if (j > 0) {
                    json.append(',');
                }

                json.append("{\"idGenero\":")
                        .append(g.getIdGenero())
                        .append(",\"nome\":\"")
                        .append(escaparJson(g.getNome()))
                        .append("\"}");
            }

            json.append("]}");
        }

        json.append("]}");

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().write(json.toString());
    }

    private void listarGenerosJson(
            HttpServletResponse response
    ) throws SQLException, IOException {

        List<Genero> generos = dao.listarGenerosAtivos();
        StringBuilder json = new StringBuilder("{\"generos\":[");

        for (int i = 0; i < generos.size(); i++) {
            Genero g = generos.get(i);

            if (i > 0) {
                json.append(',');
            }

            json.append("{\"idGenero\":")
                    .append(g.getIdGenero())
                    .append(",\"nome\":\"")
                    .append(escaparJson(g.getNome()))
                    .append("\"}");
        }

        json.append("]}");

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json.toString());
    }

    private String escaparJson(String valor) {
        if (valor == null) {
            return "";
        }

        StringBuilder out = new StringBuilder();

        for (int i = 0; i < valor.length(); i++) {
            char c = valor.charAt(i);

            switch (c) {
                case '\\':
                    out.append("\\\\");
                    break;
                case '"':
                    out.append("\\\"");
                    break;
                case '\n':
                    out.append("\\n");
                    break;
                case '\r':
                    out.append("\\r");
                    break;
                case '\t':
                    out.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
            }
        }

        return out.toString();
    }

    private void carregarGeneros(HttpServletRequest request)
            throws SQLException {

        request.setAttribute("generos", dao.listarGenerosAtivos());
        request.setAttribute("anoAtual", Year.now().getValue());
    }

    private List<Integer> listarAnos() throws SQLException {
        List<Integer> anos = new ArrayList<>();

        String sql = "SELECT DISTINCT ANO FROM FILME "
                + "WHERE ATIVO = TRUE ORDER BY ANO DESC";

        try (
                Connection c = ConnectionFactory.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                anos.add(rs.getInt(1));
            }
        }

        return anos;
    }

    private String validar(Filme filme) {
        if (filme.getTitulo() == null || filme.getTitulo().isBlank()) {
            return "Informe o título do filme.";
        }

        if (filme.getTitulo().length() > 200) {
            return "O título deve ter no máximo 200 caracteres.";
        }

        if (filme.getAno() < 1888
                || filme.getAno() > Year.now().getValue() + 5) {
            return "Informe um ano válido.";
        }

        if (filme.getDuracao() < 1 || filme.getDuracao() > 1000) {
            return "A duração deve estar entre 1 e 1000 minutos.";
        }

        if (filme.getSinopse() != null
                && filme.getSinopse().length() > 2000) {
            return "A sinopse deve ter no máximo 2000 caracteres.";
        }

        if (filme.getPosterUrl() != null
                && filme.getPosterUrl().length() > 500) {
            return "A URL do pôster deve ter no máximo 500 caracteres.";
        }

        return null;
    }

    private static int inteiro(String valor, int padrao) {
        try {
            return Integer.parseInt(valor);
        } catch (Exception e) {
            return padrao;
        }
    }

    private static Integer inteiroOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            return Integer.valueOf(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String valor(String valor, String padrao) {
        return valor == null ? padrao : valor;
    }
}