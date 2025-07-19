package xyz.sadiulhakim.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.sadiulhakim.exception.InvalidTokenException;
import xyz.sadiulhakim.exception.TokenRequiredException;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;
import xyz.sadiulhakim.util.JwtHelper;
import xyz.sadiulhakim.util.ResponseUtility;
import xyz.sadiulhakim.util.SecurityUtility;
import xyz.sadiulhakim.util.TokenUtil;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomAuthorizationFilter.class);
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    private final List<String> permittedApis = List.of(
            "/login",
            "/validate-token",
            "/refreshToken"
    );

    private final List<String> adminAccessAPis = List.of(

    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) {

        try {

            // If the path that is being accessed is authentication, do not do any token or account checking.
            String requestURI = request.getRequestURI();
            for (String permittedApi : permittedApis) {
                if (requestURI.contains(permittedApi)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (!StringUtils.hasText(authorization) || !TokenUtil.isJwtToken(authorization)) {
                throw new TokenRequiredException();
            }

            // Extract the token from authorization text
            String token = authorization.substring("Bearer ".length());

            // Extract the username
            String username = JwtHelper.extractUsername(token);

            // Get the userDetails using username
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            User user = userService.findByUsername(username);
            if (!user.isEnabled()) {
                throw new DisabledException(String.format("User %s is disabled", user.getUsername()));
            }

            if (JwtHelper.isValidToken(token, userDetails)) {
                throw new InvalidTokenException("Invalid Token!");
            }

            checkAdminAccess(user, requestURI);

            // If the token is valid and user is not authenticated, authenticate the user
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities() // We need to pass the Granted Authority list, otherwise user would be forbidden.
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            // If the authorization does not exist, or it does not start with Bearer, simply let the program go.
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            LOGGER.error("Error Occurred in CustomAuthorizationFilter. Cause : {}", ex.getMessage());

            // If the token is Invalid send an error with the response
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", ex.getMessage());
            ResponseUtility.commitResponse(response, errorMap, 500);
        }
    }

    private void checkAdminAccess(User user, String requestURI) throws Exception {
        boolean isAdmin = SecurityUtility.isAdmin(user) || SecurityUtility.isSuperUser(user);
        for (String api : adminAccessAPis) {
            boolean matched = SecurityUtility.matchPath(api, requestURI);
            if (matched && isAdmin) {
                break;
            } else if (matched) {
                throw new AccessDeniedException("Access denied. User " + user.getUsername() + " to access URL: " + requestURI);
            }
        }
    }
}
