package xyz.sadiulhakim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.sadiulhakim.pojo.Route;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;
import xyz.sadiulhakim.util.ApiRateLimiter;
import xyz.sadiulhakim.util.JwtHelper;
import xyz.sadiulhakim.util.SecurityUtility;

import java.nio.file.AccessDeniedException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteService {
    private final UserService userService;
    private final ApiRateLimiter rateLimiter;

    public ResponseEntity<?> route(Route route, String token) {
        try {
            String username = JwtHelper.extractUsername(token);
            User user = userService.findByUsername(username);
            boolean hasAccess = SecurityUtility.checkApiAccess(user, route.getPath());
            if (!hasAccess) {
                throw new AccessDeniedException("Access denied. Hi " + user.getUsername() + ", " +
                        "It looks like you don't currently have the necessary permissions to access this request." +
                        "If you're supposed to have access then please contact your administrator or support team for assistance.");
            }
            SecurityUtility.checkRateLimit(user, route, rateLimiter);

        } catch (Exception ex) {
            log.error("Could not route to path {}. Error {}", route.getPath(), ex.getMessage());
        }

        return null;
    }
}
