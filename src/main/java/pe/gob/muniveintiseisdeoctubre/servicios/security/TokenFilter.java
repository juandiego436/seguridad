package pe.gob.muniveintiseisdeoctubre.servicios.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AuthDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.SessionService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

public class TokenFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(TokenFilter.class);

    final SessionService sessionService;

    public TokenFilter(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);
        LOG.debug(((HttpServletRequest) request).getHeader("Authorization"));
        if (token != null) {
            token = StringUtils.replace(token, "Bearer", "").trim();
            final AuthDTO authDTO = sessionService.validate(token);

            if (authDTO != null && authDTO.getSession().getExpiresAt().after(new Date()) && authDTO.getAccount().getBannedAt() == null) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authDTO, token, null));
            }
        }
        chain.doFilter(request, response);
    }
}
