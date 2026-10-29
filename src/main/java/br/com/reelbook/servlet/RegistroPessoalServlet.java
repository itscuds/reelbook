
package br.com.reelbook.servlet;

import br.com.reelbook.dao.RegistroPessoalDAO;
import br.com.reelbook.model.RegistroPessoal;
import br.com.reelbook.model.Usuario;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/registro-pessoal")
public class RegistroPessoalServlet extends HttpServlet {

    private final RegistroPessoalDAO dao = new RegistroPessoalDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession sessao = request.getSession(false);
        Usuario usuario = sessao == null
                ? null
                : (Usuario) sessao.getAttribute("usuarioLogado");

        if (usuario == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Faça login para alterar sua lista.");
            return;
        }

        String acao = request.getParameter("acao");

        try {
            int idFilme = Integer.parseInt(request.getParameter("idFilme"));
            int idUsuario = usuario.getId();

            if ("remover".equals(acao)) {
                dao.remover(idUsuario, idFilme);

            } else if ("adicionar".equals(acao)
                    || "atualizar".equals(acao)) {

                RegistroPessoal registro = null;

                for (RegistroPessoal item : dao.listarPorUsuario(idUsuario)) {
                    if (item.getIdFilme() == idFilme) {
                        registro = item;
                        break;
                    }
                }

                boolean novoRegistro = registro == null;

                if (novoRegistro) {
                    registro = new RegistroPessoal();
                    registro.setIdUsuario(idUsuario);
                    registro.setIdFilme(idFilme);
                    registro.setStatus("QUERO_ASSISTIR");
                    registro.setFavorito(false);
                    registro.setDataRegistro(LocalDate.now());
                }

                String status = request.getParameter("status");
                if (status != null && !status.trim().isEmpty()) {
                    if (!status.equals("QUERO_ASSISTIR")
                            && !status.equals("ASSISTIDO")) {
                        response.sendError(
                                HttpServletResponse.SC_BAD_REQUEST,
                                "Status inválido.");
                        return;
                    }
                    registro.setStatus(status);
                }

                String favorito = request.getParameter("favorito");
                if (favorito != null) {
                    registro.setFavorito(
                            Boolean.parseBoolean(favorito));
                }

                String nota = request.getParameter("nota");
                if (nota != null && !nota.trim().isEmpty()) {
                    BigDecimal valorNota;

                    try {
                        valorNota = new BigDecimal(
                                nota.trim().replace(",", "."));
                    } catch (NumberFormatException e) {
                        response.sendError(
                                HttpServletResponse.SC_BAD_REQUEST,
                                "Nota inválida.");
                        return;
                    }

                    if (valorNota.compareTo(BigDecimal.ZERO) < 0
                            || valorNota.compareTo(
                                    new BigDecimal("10")) > 0) {
                        response.sendError(
                                HttpServletResponse.SC_BAD_REQUEST,
                                "A nota deve estar entre 0 e 10.");
                        return;
                    }

                    registro.setNota(valorNota);
                }

                if (novoRegistro) {
                    dao.adicionar(registro);
                } else {
                    dao.atualizar(registro);
                }

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Ação inválida.");
                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/minha-lista?tipo=favoritos");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Identificador inválido.");
        } catch (SQLException e) {
            throw new ServletException(
                    "Não foi possível atualizar a lista pessoal.", e);
        }
    }
}
