package br.com.reelbook.servlet;

import br.com.reelbook.dao.ResenhaDAO;
import br.com.reelbook.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/resenhas")
public class ResenhaServlet extends HttpServlet {

    private final ResenhaDAO dao = new ResenhaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");

        if ("diario".equals(acao)) {
            Usuario logado = getUsuarioLogado(request);
            if (logado == null) {
                response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
                return;
            }

            try {
                request.setAttribute("resenhasDiario", dao.listarPorUsuario(logado.getIdUsuario()));
                request.getRequestDispatcher("/pages/diario.jsp").forward(request, response);
            } catch (SQLException e) {
                throw new ServletException("Erro ao listar diário", e);
            }
            return;
        }

        response.sendRedirect(request.getContextPath() + "/filmes");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Usuario logado = getUsuarioLogado(request);

        if (logado == null) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        String acao = request.getParameter("acao");

        try {
            if ("salvar".equals(acao)) {
                String idFilmeParam = request.getParameter("idFilme");
                String texto = request.getParameter("texto");

                if (idFilmeParam != null && texto != null && !texto.isBlank()) {
                    int idFilme = Integer.parseInt(idFilmeParam);
                    // Chamando o método implementado no DAO: salvar(int, int, String)
                    dao.salvar(logado.getIdUsuario(), idFilme, texto.trim());
                    response.sendRedirect(request.getContextPath() + "/filmes?acao=detalhe&id=" + idFilme);
                    return;
                }

                response.sendRedirect(request.getContextPath() + "/filmes");
                return;
            }

            if ("editar".equals(acao)) {
                String idResenhaParam = request.getParameter("idResenha");
                String texto = request.getParameter("texto");
                String redirecionar = request.getParameter("origem");

                if (idResenhaParam != null && texto != null && !texto.isBlank()) {
                    int idResenha = Integer.parseInt(idResenhaParam);
                    // Chamando o método implementado no DAO: atualizar(int, int, String)
                    dao.atualizar(idResenha, logado.getIdUsuario(), texto.trim());
                }

                redirecionarAposAcao(request, response, redirecionar);
                return;
            }

            if ("excluir".equals(acao)) {
                String idResenhaParam = request.getParameter("idResenha");
                String redirecionar = request.getParameter("origem");

                if (idResenhaParam != null) {
                    int idResenha = Integer.parseInt(idResenhaParam);
                    dao.excluir(idResenha, logado.getIdUsuario());
                }

                redirecionarAposAcao(request, response, redirecionar);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/filmes");

        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Erro ao processar resenha", e);
        }
    }

    private void redirecionarAposAcao(HttpServletRequest request, HttpServletResponse response, String origem) throws IOException {
        if ("perfil".equals(origem)) {
            response.sendRedirect(request.getContextPath() + "/usuario?acao=perfil");
        } else {
            response.sendRedirect(request.getContextPath() + "/resenhas?acao=diario");
        }
    }

    private Usuario getUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;
    }
}