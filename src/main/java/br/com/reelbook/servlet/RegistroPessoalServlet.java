
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
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

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

        String acao = request.getParameter("acao");
        String origem = request.getParameter("origem");

        try {
            int idFilme = Integer.parseInt(
                    request.getParameter("idFilme")
            );

            int idUsuario = usuario.getId();

            if ("remover".equals(acao)) {

                dao.remover(idUsuario, idFilme);

                redirecionar(request, response, origem, null, false);
                return;
            }

            if (!"adicionar".equals(acao)
                    && !"atualizar".equals(acao)) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Ação inválida."
                );
                return;
            }

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

                if (!"QUERO_ASSISTIR".equals(status)
                        && !"ASSISTIDO".equals(status)) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Status inválido."
                    );
                    return;
                }

                registro.setStatus(status);
            }

            String favorito = request.getParameter("favorito");

            if (favorito != null) {
                registro.setFavorito(
                        Boolean.parseBoolean(favorito)
                );
            }

            String nota = request.getParameter("nota");

            if (nota != null && !nota.trim().isEmpty()) {

                BigDecimal valorNota;

                try {
                    valorNota = new BigDecimal(
                            nota.trim().replace(",", ".")
                    );
                } catch (NumberFormatException e) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Nota inválida."
                    );
                    return;
                }

                if (valorNota.compareTo(BigDecimal.ZERO) < 0
                        || valorNota.compareTo(
                        new BigDecimal("10")) > 0) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "A nota deve estar entre 0 e 10."
                    );
                    return;
                }

                registro.setNota(valorNota);
            }

            if (novoRegistro) {
                dao.adicionar(registro);
            } else {
                dao.atualizar(registro);
            }

            redirecionar(
                    request,
                    response,
                    origem,
                    registro.getStatus(),
                    registro.isFavorito()
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Identificador inválido."
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Não foi possível atualizar a lista pessoal.", e
            );
        }
    }

    private void redirecionar(
            HttpServletRequest request,
            HttpServletResponse response,
            String origem,
            String status,
            boolean favorito
    ) throws IOException {

        String destino;

        if ("diario".equals(origem)) {
            destino = "/minha-lista?tipo=diario";

        } else if ("favoritos".equals(origem)) {
            destino = "/minha-lista?tipo=favoritos";

        } else if ("lista".equals(origem)) {
            destino = "/minha-lista?tipo=lista";

        } else if ("ASSISTIDO".equalsIgnoreCase(
                status == null ? "" : status)) {

            destino = "/minha-lista?tipo=diario";

        } else if (favorito) {
            destino = "/minha-lista?tipo=favoritos";

        } else {
            destino = "/minha-lista?tipo=lista";
        }

        response.sendRedirect(
                request.getContextPath() + destino
        );
    }
}
