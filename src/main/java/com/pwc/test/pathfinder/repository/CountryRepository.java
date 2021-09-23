package com.pwc.test.pathfinder.repository;

import com.pwc.test.pathfinder.to.Country;

import java.util.List;

public interface CountryRepository {

    /**
     * Returns a list of all available {@link Country}s.
     */
    List<Country> getAllCountries();

    /**
     * Returns a {@link Country} for specified country code.
     *
     * @param countryCode the country code; must not be blank or {@code null}
     *
     * @return an instance of {@link Country} or {@code null} if not found
     *
     * @throws NullPointerException if country code is {@code null}
     * @throws IllegalArgumentException if country code is blank
     */
    Country getCountryByCode(final String countryCode);
}
