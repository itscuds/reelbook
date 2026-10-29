package br.com.reelbook.servlet;

import br.com.reelbook.dao.UsuarioDAO;
import br.com.reelbook.model.Usuario;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (email == null || senha == null
                || email.trim().isEmpty() || senha.isEmpty()) {

            response.sendRedirect(
                    request.getContextPath() + "/pages/login.jsp?erro=1"
            );
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(
                    email.trim(),
                    senha
            );

            if (usuario == null) {
                response.sendRedirect(
                        request.getContextPath() + "/pages/login.jsp?erro=1"
                );
                return;
            }

            HttpSession sessao = request.getSession(true);

            // Evita reutilizar a sessão anterior após o login.
            request.changeSessionId();

            sessao.setAttribute("usuarioLogado", usuario);
            sessao.setAttribute("idUsuario", usuario.getId());

            response.sendRedirect(request.getContextPath() + "/");

        } catch (SQLException e) {
            throw new ServletException(
                    "Erro ao autenticar usuário.", e
            );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.sendRedirect(
                request.getContextPath() + "/pages/login.jsp"
        );
    }
}