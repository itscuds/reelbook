package br.com.reelbook.servlet;
import br.com.reelbook.dao.GeneroDAO;
import br.com.reelbook.model.Genero;
import br.com.reelbook.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Administração de gêneros (só perfil CURADOR).
 * GET  /generos.jsp                    -> lista
 * POST /generos.jsp  acao=cadastrar    -> novo gênero
 * POST /generos.jsp  acao=renomear     -> renomeia / edita descrição e ordem
 * POST /generos.jsp  acao=desativar    -> ATIVO = FALSE
 * POST /generos.jsp  acao=reativar     -> ATIVO = TRUE
 */
@WebServlet("/generos.jsp")
public class GeneroServlet extends HttpServlet {

    private final GeneroDAO dao = new GeneroDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!autorizado(request, response)) {
            return;
        }
        request.setCharacterEncoding("UTF-8");

        try {
            request.setAttribute("generos.jsp", dao.listarTodos());
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar gêneros.", e);
        }

        // mensagens vindas do redirect (?msg=... / ?erro=...)
        request.setAttribute("mensagem", request.getParameter("msg"));
        request.setAttribute("erro", request.getParameter("erro"));

        request.getRequestDispatcher("/pages/generos.jsp.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!autorizado(request, response)) {
            return;
        }
        request.setCharacterEncoding("UTF-8");

        String acao = request.getParameter("acao");
        if (acao == null) {
            acao = "";
        }

        try {
            switch (acao) {
                case "cadastrar":
                    cadastrar(request, response);
                    return;
                case "renomear":
                    renomear(request, response);
                    return;
                case "desativar":
                    alterarAtivo(request, response, false);
                    return;
                case "reativar":
                    alterarAtivo(request, response, true);
                    return;
                default:
                    response.sendError(400, "Ação inválida");
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao salvar gênero.", e);
        }
    }

    private void cadastrar(HttpServletRequest request,
                           HttpServletResponse response)
            throws SQLException, IOException {

        String nome = texto(request.getParameter("nome"));

        if (nome.isEmpty() || nome.length() > 60) {
            voltar(request, response, "erro",
                    "Informe um nome de até 60 caracteres.");
            return;
        }
        if (dao.existeNome(nome, 0)) {
            voltar(request, response, "erro",
                    "Já existe um gênero com esse nome.");
            return;
        }

        Genero g = new Genero();
        g.setNome(nome);
        g.setDescricao(descricao(request));
        g.setOrdem(ordem(request.getParameter("ordem")));
        dao.cadastrar(g);

        voltar(request, response, "msg", "Gênero cadastrado.");
    }

    private void renomear(HttpServletRequest request,
                          HttpServletResponse response)
            throws SQLException, IOException {

        int id = inteiro(request.getParameter("id"));
        String nome = texto(request.getParameter("nome"));

        if (id <= 0 || dao.buscarPorId(id) == null) {
            response.sendError(404, "Gênero não encontrado");
            return;
        }
        if (nome.isEmpty() || nome.length() > 60) {
            voltar(request, response, "erro",
                    "Informe um nome de até 60 caracteres.");
            return;
        }
        if (dao.existeNome(nome, id)) {
            voltar(request, response, "erro",
                    "Já existe um gênero com esse nome.");
            return;
        }

        Genero g = new Genero();
        g.setIdGenero(id);
        g.setNome(nome);
        g.setDescricao(descricao(request));
        g.setOrdem(ordem(request.getParameter("ordem")));
        dao.atualizar(g);

        voltar(request, response, "msg", "Gênero atualizado.");
    }

    private void alterarAtivo(HttpServletRequest request,
                              HttpServletResponse response, boolean ativo)
            throws SQLException, IOException {

        int id = inteiro(request.getParameter("id"));

        if (id <= 0 || !dao.alterarAtivo(id, ativo)) {
            response.sendError(404, "Gênero não encontrado");
            return;
        }
        voltar(request, response, "msg",
                ativo ? "Gênero reativado." : "Gênero desativado.");
    }

    // ---------- auxiliares ----------

    /** Só CURADOR entra. Visitante vai para o login; usuário comum recebe 403. */
    private boolean autorizado(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {

        HttpSession sessao = request.getSession(false);
        Usuario u = sessao == null
                ? null
                : (Usuario) sessao.getAttribute("usuarioLogado");

        if (u == null) {
            response.sendRedirect(
                    request.getContextPath() + "/pages/login.jsp");
            return false;
        }
        if (!"CURADOR".equals(u.getPerfil())) {
            response.sendError(403, "Apenas curadores podem gerenciar gêneros.");
            return false;
        }
        return true;
    }

    private void voltar(HttpServletRequest request,
                        HttpServletResponse response,
                        String tipo, String texto) throws IOException {
        response.sendRedirect(request.getContextPath() + "/generos.jsp?" + tipo
                + "=" + java.net.URLEncoder.encode(texto, "UTF-8"));
    }

    private String texto(String v) {
        return v == null ? "" : v.trim();
    }

    private String descricao(HttpServletRequest request) {
        String d = texto(request.getParameter("descricao"));
        if (d.isEmpty()) {
            return null;
        }
        return d.length() > 255 ? d.substring(0, 255) : d;
    }

    private Integer ordem(String v) {
        try {
            return (v == null || v.trim().isEmpty())
                    ? null : Integer.valueOf(v.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int inteiro(String v) {
        try {
            return Integer.parseInt(v);
        } catch (Exception e) {
            return -1;
        }
    }
}
