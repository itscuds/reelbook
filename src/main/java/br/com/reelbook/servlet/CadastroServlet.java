package br.com.reelbook.servlet;

import br.com.reelbook.dao.UsuarioDAO;
import br.com.reelbook.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("usuarioId") != null) {
            resp.sendRedirect(req.getContextPath() + "/perfil");
            return;
        }
        req.getRequestDispatcher("/pages/cadastro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        String confirmaSenha = req.getParameter("confirmaSenha");

        if (nome == null || nome.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                senha == null || senha.trim().isEmpty() ||
                confirmaSenha == null || confirmaSenha.trim().isEmpty()) {

            req.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            req.setAttribute("nome", nome);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/pages/cadastro.jsp").forward(req, resp);
            return;
        }

        if (!senha.equals(confirmaSenha)) {
            req.setAttribute("erro", "As senhas informadas não coincidem.");
            req.setAttribute("nome", nome);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/pages/cadastro.jsp").forward(req, resp);
            return;
        }

        try {
            if (usuarioDAO.existeEmail(email.trim())) {
                req.setAttribute("erro", "Este e-mail já está cadastrado.");
                req.setAttribute("nome", nome);
                req.getRequestDispatcher("/pages/cadastro.jsp").forward(req, resp);
                return;
            }

            Usuario novoUsuario = new Usuario(nome.trim(), email.trim(), senha);
            usuarioDAO.cadastrar(novoUsuario);

            HttpSession sessaoAntiga = req.getSession(false);
            if (sessaoAntiga != null) {
                sessaoAntiga.invalidate();
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("usuarioId", novoUsuario.getId());
            session.setAttribute("usuarioLogado", novoUsuario);

            resp.sendRedirect(req.getContextPath() + "/perfil");

        } catch (SQLException e) {
            throw new ServletException("Erro ao cadastrar usuário no banco de dados.", e);
        } catch (Exception e) {
            throw new ServletException("Erro inesperado no cadastro.", e);
        }
    }
}