
package br.com.reelbook.servlet;

import br.com.reelbook.dao.DiarioDAO;
import br.com.reelbook.dao.RegistroPessoalDAO;
import br.com.reelbook.model.RegistroPessoalDTO;
import br.com.reelbook.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@WebServlet("/minha-lista")
public class MinhaListaServlet extends HttpServlet {

    private final RegistroPessoalDAO dao = new RegistroPessoalDAO();
    private final DiarioDAO diarioDAO = new DiarioDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession sessao = request.getSession(false);

        Usuario usuario = sessao == null
                ? null
                : (Usuario) sessao.getAttribute("usuarioLogado");

        if (usuario == null) {
            response.sendRedirect(
                    request.getContextPath() + "/pages/login.jsp"
            );
            return;
        }

        String tipo = request.getParameter("tipo");

        if (tipo == null || tipo.trim().isEmpty()) {
            tipo = "lista";
        } else {
            tipo = tipo.trim().toLowerCase(Locale.ROOT);
        }

        // Valida o tipo de lista antes de consultar o banco.
        if (!"lista".equals(tipo)
                && !"favoritos".equals(tipo)
                && !"diario".equals(tipo)) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Tipo de lista inválido."
            );
            return;
        }

        try {

            // O Diário utiliza seu próprio DAO e DTO.
            if ("diario".equals(tipo)) {

                List<DiarioDAO.DiarioDTO> diario =
                        diarioDAO.listarPorUsuario(usuario.getId());

                request.setAttribute("diario", diario);

                request.getRequestDispatcher(
                        "/pages/diario.jsp"
                ).forward(request, response);

                return;
            }

            // Lista pessoal e favoritos utilizam o RegistroPessoalDAO.
            List<RegistroPessoalDTO> todos =
                    dao.listarComFilmesPorUsuario(usuario.getId());

            List<RegistroPessoalDTO> filtrados = new ArrayList<>();

            for (RegistroPessoalDTO item : todos) {

                boolean favorito = item.getRegistro().isFavorito();

                if ("favoritos".equals(tipo) && favorito) {
                    filtrados.add(item);

                } else if ("lista".equals(tipo)) {
                    filtrados.add(item);
                }
            }

            request.setAttribute("registros", filtrados);

            if ("favoritos".equals(tipo)) {

                request.getRequestDispatcher(
                        "/pages/favoritos.jsp"
                ).forward(request, response);

            } else {

                request.getRequestDispatcher(
                        "/pages/minha-lista.jsp"
                ).forward(request, response);
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Erro ao carregar os registros pessoais.", e
            );
        }
    }
}
