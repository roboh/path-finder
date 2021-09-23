package com.pwc.test.pathfinder;

import com.pwc.test.pathfinder.to.Country;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TestConstants {

    public static class BlankValuesProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(" ", "   ", "\t", "\n").map(Arguments::of);
        }
    }

    public static final String TEST_COUNTRIES_CLASSPATH_FILE = "/test-countries.json";
    public static final List<Country> TEST_COUNTRIES;
    public static final Map<String, Country> TEST_COUNTRIES_MAPPING;

    static {
        TEST_COUNTRIES_MAPPING = new HashMap<>();
        TEST_COUNTRIES_MAPPING.put("CZE", new Country("CZE", Arrays.asList("AUT", "SVK")));
        TEST_COUNTRIES_MAPPING.put("SVK", new Country("SVK", Arrays.asList("AUT", "CZE", "HUN")));
        TEST_COUNTRIES_MAPPING.put("HUN", new Country("HUN", Arrays.asList("SVK", "AUT")));
        TEST_COUNTRIES_MAPPING.put("AUT", new Country("AUT", Arrays.asList("ITA", "CZE", "HUN", "SVK")));
        TEST_COUNTRIES_MAPPING.put("ITA", new Country("ITA", List.of("AUT")));
        TEST_COUNTRIES_MAPPING.put("FIN", new Country("FIN", List.of("NOR")));
        TEST_COUNTRIES_MAPPING.put("NOR", new Country("NOR", List.of("FIN")));

        TEST_COUNTRIES = Arrays.asList(
                TEST_COUNTRIES_MAPPING.get("CZE"),
                TEST_COUNTRIES_MAPPING.get("SVK"),
                TEST_COUNTRIES_MAPPING.get("HUN"),
                TEST_COUNTRIES_MAPPING.get("AUT"),
                TEST_COUNTRIES_MAPPING.get("ITA"),
                TEST_COUNTRIES_MAPPING.get("FIN"),
                TEST_COUNTRIES_MAPPING.get("NOR"));
    }
}
