package xyz.sadiulhakim.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import xyz.sadiulhakim.annotation.RequireAccessToken;
import xyz.sadiulhakim.pojo.Route;
import xyz.sadiulhakim.service.RouteService;

import java.util.Collections;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @RequireAccessToken
    @PostMapping("/route")
    public ResponseEntity<?> route(@RequestBody Route route, @RequestHeader("Authorization") String token) {

        try {
            return routeService.route(route, token);
        }catch (Exception e) {
            log.error("Failed to route. Error {}.", e.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
