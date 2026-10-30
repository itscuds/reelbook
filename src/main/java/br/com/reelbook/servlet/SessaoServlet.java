
package br.com.reelbook.servlet;

import br.com.reelbook.model.Usuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/sessao")
public class SessaoServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");

        HttpSession sessao = request.getSession(false);

        Usuario usuario = sessao == null
                ? null
                : (Usuario) sessao.getAttribute("usuarioLogado");

        if (usuario == null) {
            response.getWriter().write("{\"logado\":false}");
            return;
        }

        String nome = escaparJson(usuario.getNome());

        String json = "{"
                + "\"logado\":true,"
                + "\"id\":" + usuario.getId() + ","
                + "\"nome\":\"" + nome + "\""
                + "}";

        response.getWriter().write(json);
    }

    private String escaparJson(String valor) {

        if (valor == null) {
            return "";
        }

        return valor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
