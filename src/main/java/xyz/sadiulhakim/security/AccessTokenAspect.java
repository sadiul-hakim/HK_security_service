package xyz.sadiulhakim.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;
import xyz.sadiulhakim.util.JwtHelper;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessTokenAspect {

    private final UserService userService;

    @Around("@annotation(RequireAccessToken)")
    public Object validateAccessToken(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        HttpServletResponse response = attr.getResponse();
        assert response != null;
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(authorization)) {

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body(Map.of("message", "Access Token is empty."));
        }

        String username = JwtHelper.extractUsername(authorization);
        User user = userService.findByUsername(username);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body(Map.of("message", "Token " + authorization + " has no corresponding user. Refusing access to database."));
        }

        return joinPoint.proceed();
    }
}
