package com.pwc.test.pathfinder.service;

import java.util.List;

public interface CountryService {

    /**
     * Finds path, represented as a list of country codes, between the 2 countries specified by their country codes.
     *
     * @param originCountryCode      the origin country code; must not be blank or {@code null} and must be a valid country code
     * @param destinationCountryCode the destination country code; must not be blank or {@code null} and must be a valid country code
     * @return a list of country codes representing the path from origin to destination country or {@code null} if no such path can be found
     * @throws NullPointerException     if either origin or destination country code is {@code null}
     * @throws IllegalArgumentException if either origin or destination country code is blank or there's no country for specified country code
     */
    List<String> findPath(final String originCountryCode, final String destinationCountryCode);
}
