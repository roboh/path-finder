package com.pwc.test.pathfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwc.test.pathfinder.to.RoutingResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PathFinderApplicationTests {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	public void testGetRoute_BlankOrigin_ReturnsBadRequest() throws Exception {
		this.mockMvc.perform(get("/routing/ /CZE")).andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().string(matchesPattern(".*[Oo]rigin.*must be specified.*")));
	}

	@Test
	public void testGetRoute_NotValidOrigin_ReturnsBadRequest() throws Exception {
		this.mockMvc.perform(get("/routing/TEST/CZE")).andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().string(matchesPattern(".*code.*origin.*not valid.*")));
	}

	@Test
	public void testGetRoute_BlankDestination_ReturnsBadRequest() throws Exception {
		this.mockMvc.perform(get("/routing/CZE/ ")).andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().string(matchesPattern(".*[Dd]estination.*must be specified.*")));
	}

	@Test
	public void testGetRoute_NotValidDestination_ReturnsBadRequest() throws Exception {
		this.mockMvc.perform(get("/routing/CZE/TEST")).andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().string(matchesPattern(".*code.*destination.*not valid.*")));
	}

	@Test
	public void testGetRoute_NonExistingPath_ReturnsNotFound() throws Exception {
		this.mockMvc.perform(get("/routing/CZE/NZL")).andDo(print()).andExpect(status().isNotFound());
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/path-rest-tests.csv", numLinesToSkip = 1)
	public void testFindPath_ExistingPaths_ReturnsCorrectList(final String origin, final String destination, final String expectedListStr) throws Exception {
		final List<String> expectedPath = Arrays.asList(expectedListStr.split(";"));

		this.mockMvc.perform(get("/routing/{origin}/{destination}", origin, destination)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(OBJECT_MAPPER.writeValueAsString(new RoutingResult(expectedPath))));
	}
}
