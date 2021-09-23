package com.pwc.test.pathfinder.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwc.test.pathfinder.to.Country;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class CountryRepositoryImpl implements CountryRepository {

    private final List<Country> countries;
    private final Map<String, Country> countryCodeToCountryObjMapping;

    public CountryRepositoryImpl(@NonNull final ObjectMapper jacksonObjectMapper, @NonNull @Value("${resource.countries.json}") final Resource countriesJson) {
        try {
            this.countries = Collections.unmodifiableList(jacksonObjectMapper.readValue(countriesJson.getURL(), new TypeReference<>() {
            }));
            this.countryCodeToCountryObjMapping = Collections.unmodifiableMap(this.countries.stream().collect(Collectors.toMap(Country::getCode, Function.identity())));
        } catch (IOException e) {
            throw new UncheckedIOException("Couldn't read JSON with list of countries!", e);
        }
    }

    @Override
    public List<Country> getAllCountries() {
        return countries;
    }

    @Override
    public Country getCountryByCode(@NonNull final String countryCode) {
        Validate.notBlank(countryCode, "Country code must not be blank!");
        return countryCodeToCountryObjMapping.get(countryCode);
    }
}
