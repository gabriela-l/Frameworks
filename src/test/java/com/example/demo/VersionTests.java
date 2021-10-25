package com.example.demo;

import com.example.demo.model.Version;
import com.example.demo.repository.FrameworkRepository;
import com.example.demo.repository.VersionRepository;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class VersionTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VersionRepository repo;
    @Autowired
    private FrameworkRepository frameworkRepo;

    private ObjectMapper mapper = new ObjectMapper();

    private void prepareData() throws Exception {
        Version react = new Version();
        react.setVersion("17.0.2");
        // k jakému frameworku verze patří (cizí klíč)
        react.setFramework(frameworkRepo.getById(1));
        Version react2 = new Version();
        react2.setVersion("17.0.1");
        react2.setFramework(frameworkRepo.getById(1));
        repo.save(react);
        repo.save(react2);
    }

    // test ověřující vložení zkušebních dat:
    @Test
    public void versionsTest() throws Exception {
        prepareData();
        mockMvc.perform(get("/versions")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    // test pro smazání záznamu s id 2:
    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/versions/delete/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}