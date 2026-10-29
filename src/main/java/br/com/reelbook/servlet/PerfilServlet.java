package br.com.reelbook.servlet;

import br.com.reelbook.dao.DiarioDAO;
import br.com.reelbook.dao.ResenhaDAO;
import br.com.reelbook.dao.UsuarioDAO;
import br.com.reelbook.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final DiarioDAO diarioDAO = new DiarioDAO();
    private final ResenhaDAO resenhaDAO = new ResenhaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idUsuario = obterUsuarioId(req);

        try {
            Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
            if (usuario == null) {
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
                return;
            }

            req.setAttribute("usuario", usuario);
            req.setAttribute("stats", usuarioDAO.obterEstatisticas(idUsuario));
            req.setAttribute("diario", diarioDAO.listarPorUsuario(idUsuario));
            req.setAttribute("resenhas", resenhaDAO.listarPorUsuario(idUsuario));

            req.getRequestDispatcher("/pages/perfil.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private int obterUsuarioId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("usuarioId") != null) {
            return (Integer) session.getAttribute("usuarioId");
        }
        return 1;
    }
}