
package br.com.reelbook.servlet;

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

@WebServlet("/minha-lista")
public class MinhaListaServlet extends HttpServlet {

    private final RegistroPessoalDAO dao = new RegistroPessoalDAO();

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
        }

        try {
            List<RegistroPessoalDTO> todos =
                    dao.listarComFilmesPorUsuario(usuario.getId());

            List<RegistroPessoalDTO> filtrados = new ArrayList<>();

            for (RegistroPessoalDTO item : todos) {

                boolean favorito = item.getRegistro().isFavorito();
                String status = item.getRegistro().getStatus();

                if ("favoritos".equals(tipo) && favorito) {
                    filtrados.add(item);

                } else if ("diario".equals(tipo)
                        && "ASSISTIDO".equalsIgnoreCase(
                        status == null ? "" : status)) {

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

            } else if ("diario".equals(tipo)) {

                request.getRequestDispatcher(
                        "/pages/diario.jsp"
                ).forward(request, response);

            } else if ("lista".equals(tipo)) {

                request.getRequestDispatcher(
                        "/pages/minha-lista.jsp"
                ).forward(request, response);

            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Tipo de lista inválido."
                );
            }

        } catch (SQLException e) {
            throw new ServletException(
                    "Erro ao carregar os registros pessoais.", e
            );
        }
    }
}
