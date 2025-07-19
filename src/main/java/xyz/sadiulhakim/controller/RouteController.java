package xyz.sadiulhakim.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class RouteController {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final RouteService routeService;

    @RequireAccessToken
    @PostMapping("/route")
    public ResponseEntity<?> route(@RequestBody Route requestBody, @RequestHeader("Authorization") String token) {
        return routeService.route(requestBody,token);
    }
}
