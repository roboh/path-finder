package com.pwc.test.pathfinder.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwc.test.pathfinder.TestConstants;
import com.pwc.test.pathfinder.to.Country;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.UncheckedIOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CountryRepositoryImplTests {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static Resource countriesResource;

    private CountryRepositoryImpl instance;

    @BeforeAll
    public static void beforeClass() {
        countriesResource = new ClassPathResource(TestConstants.TEST_COUNTRIES_CLASSPATH_FILE);
    }

    @BeforeEach
    public void setUp() {
        instance = new CountryRepositoryImpl(OBJECT_MAPPER, countriesResource);
    }

    @Test
    public void testCTOR_NonExistingResource_throwsUIOE() {
        Assertions.assertThrows(UncheckedIOException.class, () -> new CountryRepositoryImpl(OBJECT_MAPPER, new ClassPathResource("/wrong-file.json")));
    }

    @ParameterizedTest
    @ArgumentsSource(TestConstants.BlankValuesProvider.class)
    public void testGetCountryByCode_BlankValue_throwsIAE(final String value) {
        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> instance.getCountryByCode(value));
        assertThat(exception.getMessage(), containsString("code must not be"));
    }

    @Test
    public void testGetCountryByCode_NullValue_throwsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> instance.getCountryByCode(null));
    }

    @Test
    public void testGetCountryByCode_ValidCode_ReturnsCountry() {
        final Country actual = instance.getCountryByCode("CZE");

        assertThat(actual, is(notNullValue()));
        assertThat(actual.getCode(), is("CZE"));
        assertThat(actual.getBorders(), contains("AUT", "SVK"));
    }

    @Test
    public void testGetCountryByCode_NonExistingCode_ReturnsNull() {
        final Country actual = instance.getCountryByCode("SWE");

        assertThat(actual, is(nullValue()));
    }

    @Test
    public void testGetAllCountries() {
        final List<Country> actual = instance.getAllCountries();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(TestConstants.TEST_COUNTRIES));
    }
}
