
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
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("diario".equals(acao)) {
            response.sendRedirect(
                    request.getContextPath() + "/minha-lista?tipo=diario"
            );
            return;
        }

        response.sendRedirect(
                request.getContextPath() + "/filmes"
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Usuario logado = getUsuarioLogado(request);

        if (logado == null) {
            response.sendRedirect(
                    request.getContextPath() + "/pages/login.jsp"
            );
            return;
        }

        String acao = request.getParameter("acao");

        try {
            if ("salvar".equals(acao)) {

                String idFilmeParam = request.getParameter("idFilme");
                String texto = request.getParameter("texto");

                if (idFilmeParam == null || texto == null
                        || texto.trim().isEmpty()) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Informe o filme e o texto da resenha."
                    );
                    return;
                }

                int idFilme = Integer.parseInt(idFilmeParam);

                dao.salvar(
                        logado.getId(),
                        idFilme,
                        texto.trim()
                );

                response.sendRedirect(
                        request.getContextPath()
                                + "/filmes?acao=detalhe&id=" + idFilme
                );
                return;
            }

            if ("editar".equals(acao)) {

                String idParam = request.getParameter("idResenha");
                String texto = request.getParameter("texto");

                if (idParam == null || texto == null
                        || texto.trim().isEmpty()) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Dados da resenha inválidos."
                    );
                    return;
                }

                int idResenha = Integer.parseInt(idParam);

                dao.atualizar(
                        idResenha,
                        logado.getId(),
                        texto.trim()
                );

                redirecionarAposAcao(request, response);
                return;
            }

            if ("excluir".equals(acao)) {

                String idParam = request.getParameter("idResenha");

                if (idParam == null) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Identificador da resenha não informado."
                    );
                    return;
                }

                int idResenha = Integer.parseInt(idParam);

                dao.excluir(
                        idResenha,
                        logado.getId()
                );

                redirecionarAposAcao(request, response);
                return;
            }

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Ação inválida."
            );

        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(
                    "Erro ao processar resenha.", e
            );
        }
    }

    private void redirecionarAposAcao(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String origem = request.getParameter("origem");

        if ("perfil".equals(origem)) {
            response.sendRedirect(
                    request.getContextPath() + "/perfil"
            );
        } else {
            response.sendRedirect(
                    request.getContextPath() + "/minha-lista?tipo=diario"
            );
        }
    }

    private Usuario getUsuarioLogado(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        return session == null
                ? null
                : (Usuario) session.getAttribute("usuarioLogado");
    }
}
