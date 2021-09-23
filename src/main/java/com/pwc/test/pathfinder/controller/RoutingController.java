package com.pwc.test.pathfinder.controller;

import com.pwc.test.pathfinder.service.CountryService;
import com.pwc.test.pathfinder.to.RoutingResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/routing")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RoutingController {

    private final CountryService countryService;

    @GetMapping(value = "/{origin}/{destination}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoutingResult getRoute(@PathVariable("origin") final String origin, @PathVariable("destination") final String destination) {
        Validate.notBlank(origin, "'origin' must be specified");
        Validate.notBlank(destination, "'destination' must be specified");

        final List<String> path = countryService.findPath(origin, destination);
        if (path == null || path.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new RoutingResult(path);
    }
}
