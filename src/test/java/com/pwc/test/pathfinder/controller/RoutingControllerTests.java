package com.pwc.test.pathfinder.controller;

import com.pwc.test.pathfinder.TestConstants;
import com.pwc.test.pathfinder.service.CountryService;
import com.pwc.test.pathfinder.to.RoutingResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RoutingControllerTests {

    @Mock
    private CountryService countryService;

    private RoutingController instance;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        instance = new RoutingController(countryService);
    }

    @ParameterizedTest
    @ArgumentsSource(TestConstants.BlankValuesProvider.class)
    public void testGetRoute_OriginBlankValue_throwsIAE(final String value) {
        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> instance.getRoute(value, "CZE"));
        assertThat(exception.getMessage(), matchesRegex(".*[Oo]rigin.*must be specified.*"));
    }

    @Test
    public void testGetRoute_OriginNullValue_throwsNPE() {
        final NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> instance.getRoute(null, "CZE"));
        assertThat(exception.getMessage(), containsString("origin"));
    }

    @ParameterizedTest
    @ArgumentsSource(TestConstants.BlankValuesProvider.class)
    public void testGetRoute_DestinationBlankValue_throwsIAE(final String value) {
        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> instance.getRoute("CZE", value));
        assertThat(exception.getMessage(), matchesRegex(".*[dD]estination.*must be specified.*"));
    }

    @Test
    public void testGetRoute_DestinationNullValue_throwsNPE() {
        final NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> instance.getRoute("CZE", null));
        assertThat(exception.getMessage(), containsString("destination"));
    }

    @Test
    public void testGetRoute_NullPath_throwsRSE() {
        Mockito.when(countryService.findPath("CZE", "ITA")).thenReturn(null);
        final ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> instance.getRoute("CZE", "ITA"));
        assertThat(exception.getStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testGetRoute_EmptyPath_throwsRSE() {
        Mockito.when(countryService.findPath("CZE", "ITA")).thenReturn(Collections.emptyList());
        final ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> instance.getRoute("CZE", "ITA"));
        assertThat(exception.getStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testGetRoute_ExistingPath_CorrectResult() {
        final List<String> expectedPath = Arrays.asList("CZE", "AUT", "ITA");
        Mockito.when(countryService.findPath("CZE", "ITA")).thenReturn(expectedPath);

        final RoutingResult actual = instance.getRoute("CZE", "ITA");
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(new RoutingResult(expectedPath)));
    }
}
