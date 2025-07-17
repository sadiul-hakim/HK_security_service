package xyz.sadiulhakim.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class FilterChainExceptionHandler extends OncePerRequestFilter {

    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public void setHandlerExceptionResolver(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Spring Security Filter Chain Exception:", e);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
