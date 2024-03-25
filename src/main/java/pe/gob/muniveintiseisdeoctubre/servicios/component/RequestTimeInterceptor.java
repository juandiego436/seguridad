package pe.gob.muniveintiseisdeoctubre.servicios.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestTimeInterceptor extends HandlerInterceptorAdapter {

    private static final Log LOG = LogFactory.getLog(RequestTimeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        LOG.info("-- START Request URL: '" + request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        LOG.info("-- FINISH Request URL: '" + request.getRequestURI());
        LOG.info("-- TOTAL TIME: " + (System.currentTimeMillis() - startTime) + "'ms");
    }
}