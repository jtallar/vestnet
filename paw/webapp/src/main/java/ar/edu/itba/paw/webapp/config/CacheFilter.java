package ar.edu.itba.paw.webapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CacheFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheFilter.class);

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {

        /* All the requests not to the API (front files mostly) */
        if(!request.getRequestURI().matches(".*/api/.*")) {
            LOGGER.debug("Cache-Control added to request " + request.getRequestURI());
            response.setHeader("Cache-Control", "max-age=31536000, public"); // 1 year
        }

        response.setHeader("Connection", "Keep-Alive");
        chain.doFilter(request, response);
    }
}