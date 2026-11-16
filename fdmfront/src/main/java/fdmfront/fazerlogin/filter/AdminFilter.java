package fdmfront.fazerlogin.filter;

import java.io.IOException;

import fdmfront.fazerlogin.controller.SessaoCtrl;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// Segurança, garante que o user é admin para permitir ele a ver as coisas de admin.
@WebFilter(urlPatterns = "/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest requisicao, ServletResponse resposta, FilterChain cadeia)
            throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) requisicao;
        HttpSession sessao = http.getSession(false);
        if (sessao != null && Boolean.TRUE.equals(sessao.getAttribute(SessaoCtrl.ATRIBUTO_ADMIN))) {
            cadeia.doFilter(requisicao, resposta);
        } else {
            ((HttpServletResponse) resposta).sendRedirect(http.getContextPath() + "/login.xhtml");
        }
    }
}
