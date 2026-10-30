
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
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        Usuario logado = session == null
                ? null
                : (Usuario) session.getAttribute("usuarioLogado");

        if (logado == null) {
            resp.sendRedirect(
                    req.getContextPath() + "/pages/login.jsp"
            );
            return;
        }

        int idUsuario = logado.getId();

        try {
            Usuario usuario = usuarioDAO.buscarPorId(idUsuario);

            if (usuario == null) {
                resp.sendRedirect(
                        req.getContextPath() + "/logout"
                );
                return;
            }

            req.setAttribute("usuario", usuario);
            req.setAttribute(
                    "stats",
                    usuarioDAO.obterEstatisticas(idUsuario)
            );
            req.setAttribute(
                    "diario",
                    diarioDAO.listarPorUsuario(idUsuario)
            );
            req.setAttribute(
                    "resenhas",
                    resenhaDAO.listarPorUsuario(idUsuario)
            );

            req.getRequestDispatcher("/pages/perfil.jsp")
                    .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(
                    "Erro ao carregar o perfil.", e
            );
        }
    }
}
