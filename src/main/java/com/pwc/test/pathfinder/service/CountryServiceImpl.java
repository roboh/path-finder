package com.pwc.test.pathfinder.service;

import com.pwc.test.pathfinder.repository.CountryRepository;
import com.pwc.test.pathfinder.to.Country;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final DijkstraShortestPath<Country, DefaultEdge> shortestPathAlg;

    @Autowired
    public CountryServiceImpl(final CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
        this.shortestPathAlg = initShortestPathAlg(countryRepository);
    }

    @Override
    public List<String> findPath(@NonNull final String originCountryCode, @NonNull final String destinationCountryCode) {
        Validate.notBlank(originCountryCode, "Origin country code must be specified!");
        Validate.notBlank(destinationCountryCode, "Destination country code must be specified!");

        final Country origin = countryRepository.getCountryByCode(originCountryCode);
        if (origin == null) {
            throw new IllegalArgumentException("Country code '" + originCountryCode + "' specified for origin is not valid!");
        }

        final Country destination = countryRepository.getCountryByCode(destinationCountryCode);
        if (destination == null) {
            throw new IllegalArgumentException("Country code '" + destinationCountryCode + "' specified for destination is not valid!");
        }

        final GraphPath<Country, DefaultEdge> path = shortestPathAlg.getPath(origin, destination);
        return path != null ? path.getVertexList().stream().map(Country::getCode).collect(Collectors.toList()) : null;
    }

    /**
     * Initializes {@link DijkstraShortestPath} algorithm for searching shortest path between countries.
     */
    private DijkstraShortestPath<Country, DefaultEdge> initShortestPathAlg(final CountryRepository countryRepository) {
        final Graph<Country, DefaultEdge> countriesGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        final List<Country> countries = countryRepository.getAllCountries();
        countries.forEach(countriesGraph::addVertex);
        countries.forEach(c -> c.getBorders().forEach(bc -> {
            final Country borderCountry = countryRepository.getCountryByCode(bc);
            if (borderCountry != null) {
                countriesGraph.addEdge(c, borderCountry);
            }
        }));
        return new DijkstraShortestPath<>(countriesGraph);
    }
}
