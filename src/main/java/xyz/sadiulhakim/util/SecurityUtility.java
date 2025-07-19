package xyz.sadiulhakim.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import xyz.sadiulhakim.enumeration.UserLevel;
import xyz.sadiulhakim.limit.ApiCallRateLimit;
import xyz.sadiulhakim.limit.RateLimit;
import xyz.sadiulhakim.pojo.Route;
import xyz.sadiulhakim.role.Role;
import xyz.sadiulhakim.url.AllowedUrl;
import xyz.sadiulhakim.user.User;

import java.nio.file.*;

public class SecurityUtility {

    private static final String PATH_PREFIX = "glob:";

    private SecurityUtility() {
    }

    public static boolean isAdmin(User user) {
        if (user == null) {
            return false;
        }
        return user.getLevel().equals(UserLevel.ADMIN);
    }

    public static boolean isSuperUser(User user) {
        if (user == null) {
            return false;
        }
        return user.getLevel().equals(UserLevel.SUPER);
    }

    public static boolean checkApiAccess(User user, String path) {
        if (isSuperUser(user)) return true;

        String basePath = path.split("\\?")[0]; // TODO : check this

        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .flatMap(permission -> permission.getUrls().stream())
                .anyMatch(url -> matchPath(url.getApiPattern(), basePath));
    }

    public static void checkRateLimit(User user, Route route, ApiRateLimiter rateLimiter) throws Exception {
        String userId = String.valueOf(user.getId());

        for (Role role : user.getRoles()) {
            for (ApiCallRateLimit limit : role.getApiCallRateLimits()) {
                AllowedUrl url = limit.getAllowedUrls();

                if (!matchPath(url.getApiPattern(), route.getPath()) ||
                        !url.getMethod().name().equalsIgnoreCase(route.getMethod())) {
                    continue;
                }

                for (RateLimit rateLimit : limit.getRateLimits()) {

                    if (!rateLimiter.canLoad(userId, rateLimit.getCallNumber(), rateLimit.toDuration())) {
                        throw new ResponseStatusException(
                                HttpStatus.TOO_MANY_REQUESTS,
                                "API calls limit exceeded. Please try again later."
                        );
                    }
                }
            }
        }
    }


    private static boolean matchPath(String pathPattern, String path) {

        FileSystem fileSystem = FileSystems.getDefault();
        PathMatcher pathMatcher = fileSystem.getPathMatcher(PATH_PREFIX + pathPattern);
        Path requestedPath = Paths.get(path);
        return pathMatcher.matches(requestedPath);
    }
}
