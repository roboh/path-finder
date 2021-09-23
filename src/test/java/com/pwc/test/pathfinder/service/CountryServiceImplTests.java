package com.pwc.test.pathfinder.service;

import com.pwc.test.pathfinder.TestConstants;
import com.pwc.test.pathfinder.repository.CountryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CountryServiceImplTests {

    @Mock
    private CountryRepository countryRepository;

    private CountryServiceImpl instance;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(countryRepository.getAllCountries()).thenReturn(TestConstants.TEST_COUNTRIES);
        Mockito.when(countryRepository.getCountryByCode(ArgumentMatchers.any())).then(invocation -> TestConstants.TEST_COUNTRIES_MAPPING.get(invocation.getArgument(0, String.class)));

        instance = new CountryServiceImpl(countryRepository);
    }

    @ParameterizedTest
    @ArgumentsSource(TestConstants.BlankValuesProvider.class)
    public void testFindPath_OriginCountryCodeBlankValue_throwsIAE(final String value) {
        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> instance.findPath(value, "CZE"));
        assertThat(exception.getMessage(), matchesRegex("[Oo]rigin.*must be specified.*"));
    }

    @Test
    public void testFindPath_OriginCountryCodeNullValue_throwsNPE() {
        final NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> instance.findPath(null, "CZE"));
        assertThat(exception.getMessage(), containsString("originCountryCode"));
    }

    @Test
    public void testFindPath_OriginCountryCodeNotExisting_throwsIAE() {


        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> instance.findPath("SWE", "CZE"));
        assertThat(exception.getMessage(), matchesRegex(".*code.*origin.*not valid.*"));
    }

    @ParameterizedTest
    @ArgumentsSource(TestConstants.BlankValuesProvider.class)
    public void testFindPath_DestinationCountryCodeBlankValue_throwsIAE(final String value) {
        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> instance.findPath("CZE", value));
        assertThat(exception.getMessage(), matchesRegex("[Dd]estination.*must be specified.*"));
    }

    @Test
    public void testFindPath_DestinationCountryCodeNullValue_throwsNPE() {
        final NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> instance.findPath("CZE", null));
        assertThat(exception.getMessage(), containsString("destinationCountryCode"));
    }

    @Test
    public void testFindPath_DestinationCountryCodeNotExisting_throwsIAE() {
        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> instance.findPath("CZE", "SWE"));
        assertThat(exception.getMessage(), matchesRegex(".*code.*destination.*not valid.*"));
    }

    @Test
    public void testFindPath_NoExistingPath_ReturnsNull() {
        final List<String> actual = instance.findPath("FIN", "CZE");
        assertThat(actual, is(nullValue()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/path-tests.csv", numLinesToSkip = 1)
    public void testFindPath_ExistingPaths_ReturnsCorrectList(final String origin, final String destination, final String expectedListStr) {
        final List<String> expectedPath = Arrays.asList(expectedListStr.split(";"));

        final List<String> actual = instance.findPath(origin, destination);
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(expectedPath));
    }
}
