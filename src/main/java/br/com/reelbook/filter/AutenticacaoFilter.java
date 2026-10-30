
package br.com.reelbook.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.reelbook.model.Usuario;

@WebFilter(
        urlPatterns = {
                "/pages/diario.jsp",
                "/pages/favoritos.jsp",
                "/pages/minha-lista.jsp",
                "/pages/listas.jsp",
                "/pages/perfil.jsp"
        },
        dispatcherTypes = DispatcherType.REQUEST
)
public class AutenticacaoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {
    }

    @Override
    public void doFilter(
            javax.servlet.ServletRequest request,
            javax.servlet.ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession sessao = req.getSession(false);

        Usuario usuario = sessao == null
                ? null
                : (Usuario) sessao.getAttribute("usuarioLogado");

        if (usuario == null) {
            resp.sendRedirect(
                    req.getContextPath() + "/pages/login.jsp"
            );
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
