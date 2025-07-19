package xyz.sadiulhakim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import xyz.sadiulhakim.path_config.PathConfigModel;
import xyz.sadiulhakim.path_config.PathConfigService;
import xyz.sadiulhakim.pojo.Route;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;
import xyz.sadiulhakim.util.ApiRateLimiter;
import xyz.sadiulhakim.util.JwtHelper;
import xyz.sadiulhakim.util.RouteValidator;
import xyz.sadiulhakim.util.SecurityUtility;

import java.nio.file.AccessDeniedException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteService {
    private final UserService userService;
    private final ApiRateLimiter rateLimiter;
    private final PathConfigService pathConfigService;

    private static final String USER_AGENT = "User-Agent";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String ACCESS_TOKEN = "accessToken";

    @Value("security.user.agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
    private String agent;

    public ResponseEntity<?> route(Route route, String token) {
        try {

            RouteValidator.validateRoute(route);
            String username = JwtHelper.extractUsername(token);
            User user = userService.findByUsername(username);
            boolean hasAccess = SecurityUtility.checkApiAccess(user, route.getPath());
            if (!hasAccess) {
                throw new AccessDeniedException("Access denied. Hi " + user.getUsername() + ", " +
                        "It looks like you don't currently have the necessary permissions to access this request." +
                        "If you're supposed to have access then please contact your administrator or support team for assistance.");
            }
            SecurityUtility.checkRateLimit(user, route, rateLimiter);
            PathConfigModel pathConfig = pathConfigService.getByUrlPatter(route.getPath());
            HttpMethod httpMethod = HttpMethod.valueOf(route.getMethod().toUpperCase());
            String scheme = StringUtils.hasText(pathConfig.getScheme()) ? pathConfig.getScheme() : "https";
            int port = pathConfig.getPort() > 0 ? pathConfig.getPort() : 443;
            String fullUrl = scheme + "://" + pathConfig.getDomain() + ":" + port + "/" + route.getPath();

            RestClient.RequestBodySpec bodySpec = RestClient.create(fullUrl)
                    .method(httpMethod)
                    .header(USER_AGENT, agent)
                    .header(USER_ID, String.valueOf(user.getId()))
                    .header(USER_NAME, user.getFullName())
                    .header(ACCESS_TOKEN, token)
                    .accept(MediaType.APPLICATION_JSON);

            if (httpMethod.equals(HttpMethod.POST) || httpMethod.equals(HttpMethod.PUT)) {
                bodySpec.contentType(MediaType.APPLICATION_JSON);
                bodySpec.body(route.getBody());
            }

            return bodySpec.retrieve().toEntity(String.class);

        } catch (Exception ex) {
            log.error("Could not route to path {}. Error {}", route.getPath(), ex.getMessage());
            throw new RuntimeException("Could not route to path " + route.getPath() + ". Error " + ex.getMessage());
        }
    }
}
