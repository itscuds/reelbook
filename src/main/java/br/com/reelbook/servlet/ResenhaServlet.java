
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

    private static final int LIMITE_TEXTO = 2000;

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

                if (idFilmeParam == null || texto == null) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Informe o filme e o texto da resenha."
                    );
                    return;
                }

                int idFilme = Integer.parseInt(idFilmeParam);

                String erroTexto = validarTexto(texto);

                if (erroTexto != null) {
                    response.sendRedirect(
                            request.getContextPath()
                                    + "/filmes?acao=detalhe&id=" + idFilme
                                    + "&erroResenha=" + erroTexto + "#resenhas"
                    );
                    return;
                }

                dao.salvar(
                        logado.getId(),
                        idFilme,
                        texto.trim()
                );

                response.sendRedirect(
                        request.getContextPath()
                                + "/filmes?acao=detalhe&id=" + idFilme
                                + "#resenhas"
                );
                return;
            }

            if ("editar".equals(acao)) {

                String idParam = request.getParameter("idResenha");
                String texto = request.getParameter("texto");

                if (idParam == null || texto == null) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Dados da resenha inválidos."
                    );
                    return;
                }

                String erroTexto = validarTexto(texto);

                if (erroTexto != null) {
                    String idFilmeParam = request.getParameter("idFilme");

                    if ("detalhe".equals(request.getParameter("origem"))
                            && idFilmeParam != null) {
                        response.sendRedirect(
                                request.getContextPath()
                                        + "/filmes?acao=detalhe&id="
                                        + Integer.parseInt(idFilmeParam)
                                        + "&erroResenha=" + erroTexto
                                        + "#resenhas"
                        );
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_BAD_REQUEST,
                                "vazia".equals(erroTexto)
                                        ? "Informe o texto da resenha."
                                        : "A resenha deve ter no máximo "
                                                + LIMITE_TEXTO + " caracteres."
                        );
                    }
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
        String idFilmeParam = request.getParameter("idFilme");

        if ("detalhe".equals(origem) && idFilmeParam != null) {
            try {
                response.sendRedirect(
                        request.getContextPath()
                                + "/filmes?acao=detalhe&id="
                                + Integer.parseInt(idFilmeParam)
                                + "#resenhas"
                );
                return;
            } catch (NumberFormatException e) {
                // idFilme inválido: segue para o redirecionamento padrão
            }
        }

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

    private String validarTexto(String texto) {
        String limpo = texto.trim();

        if (limpo.isEmpty()) {
            return "vazia";
        }

        if (limpo.length() > LIMITE_TEXTO) {
            return "longa";
        }

        return null;
    }

    private Usuario getUsuarioLogado(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        return session == null
                ? null
                : (Usuario) session.getAttribute("usuarioLogado");
    }
}
