package com.example.demo;

import com.example.demo.model.Framework;
import com.example.demo.repository.FrameworkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class FrameworksApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private FrameworkRepository repo;

	private ObjectMapper mapper = new ObjectMapper();

	private void prepareData() throws Exception {
		Framework react = new Framework();
		react.setName("ReactJS");
		react.setDeprecationDate(null);
		react.setHypeLevel(10);
		Framework vue = new Framework();
		vue.setName("Vue.js");
		vue.setDeprecationDate(null);
		vue.setHypeLevel(9);
		Framework angular = new Framework();
		angular.setName("Angular.js");
		angular.setHypeLevel(7);
		Date date = new GregorianCalendar(2021, Calendar.DECEMBER, 31).getTime();
		angular.setDeprecationDate(date);
		repo.save(react);
		repo.save(vue);
		repo.save(angular);
	}

	// test ověřující vytvoření zkušebních dat:
	@Test
	public void frameworksTest() throws Exception {
		prepareData();
		mockMvc.perform(get("/frameworks")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].name", is("ReactJS")))
				.andExpect(jsonPath("$[1].name", is("Vue.js")))
				.andExpect(jsonPath("$[2].name", is("Angular.js")));
	}

	// test k vyhledání záznamu s id 1 a ověření, že jde o React:
	@Test
	public void testFindFramework() throws Exception {
		mockMvc.perform(get("/frameworks/find/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("ReactJS")));
	}

	// test na vytvoření nového frameworku (záznamu)
	@Test
	public void testCreateFramework() throws Exception {
		Framework framework = new Framework();
		framework.setName("Ember.js");
		Date date = new GregorianCalendar(2022, Calendar.DECEMBER, 31).getTime();
		framework.setDeprecationDate(date);
		framework.setHypeLevel(4);
		mockMvc.perform(MockMvcRequestBuilders
				.post("/frameworks/create")
				.content(mapper.writeValueAsString(framework))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Ember.js")));
	}

	// test na editaci záznamu s id 4
	@Test
	public void testUpdateFramework() throws Exception {
		Framework updatedFramework = new Framework();
		updatedFramework.setName("Ember.js");
		Date date = new GregorianCalendar(2022, Calendar.OCTOBER, 31).getTime();
		updatedFramework.setDeprecationDate(date);
		updatedFramework.setHypeLevel(4);
		mockMvc.perform(MockMvcRequestBuilders
				.put("/frameworks/update/4")
				.content(mapper.writeValueAsString(updatedFramework))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	// test na smazání záznamu s id 4
	@Test
	public void testDeleteFramework() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/frameworks/delete/4")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
