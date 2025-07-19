package xyz.sadiulhakim.path_config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.sadiulhakim.util.SecurityUtility;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PathConfigService {
    private final PathConfigRepository repository;

    public PathConfigModel getByUrlPatter(String path) {
        List<PathConfigModel> configs = repository.findAll();
        for (PathConfigModel config : configs) {
            if (SecurityUtility.matchPath(config.getUrlPattern(), path.split("\\?")[0])) {
                return config;
            }
        }

        return null;
    }
}
